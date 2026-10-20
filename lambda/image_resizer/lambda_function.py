
import io
import logging
import os
from urllib.parse import unquote_plus

import boto3
from PIL import Image, ImageOps, UnidentifiedImageError

logger = logging.getLogger()
logger.setLevel(logging.INFO)

s3 = boto3.client("s3")

TARGET_SIZE = (800, 600)
MAX_IMAGE_BYTES = 15 * 1024 * 1024
SUPPORTED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".webp"}


def resize_image(image_bytes):
    with Image.open(io.BytesIO(image_bytes)) as image:
        image = ImageOps.exif_transpose(image)
        image.load()

        # Handle transparent PNG/WebP backgrounds.
        if image.mode in ("RGBA", "LA") or "transparency" in image.info:
            rgba = image.convert("RGBA")
            background = Image.new("RGB", rgba.size, "white")
            background.paste(rgba, mask=rgba.getchannel("A"))
            image = background
        else:
            image = image.convert("RGB")

        resized = ImageOps.fit(
            image,
            TARGET_SIZE,
            method=Image.Resampling.LANCZOS
        )

        output = io.BytesIO()
        resized.save(
            output,
            format="JPEG",
            quality=80,
            optimize=True
        )
        return output.getvalue()


def lambda_handler(event, context):
    processed_count = 0

    for record in event.get("Records", []):
        if record.get("eventSource") != "aws:s3":
            continue

        bucket = record["s3"]["bucket"]["name"]
        key = unquote_plus(record["s3"]["object"]["key"])

        # Only process objects under original/.
        if not key.startswith("original/properties/"):
            continue

        extension = os.path.splitext(key)[1].lower()
        if extension not in SUPPORTED_EXTENSIONS:
            logger.info("Skipping unsupported file: %s", key)
            continue

        size = int(record["s3"]["object"].get("size", 0))
        if size <= 0 or size > MAX_IMAGE_BYTES:
            logger.warning("Skipping invalid or oversized object: %s", key)
            continue

        response = s3.get_object(Bucket=bucket, Key=key)
        image_bytes = response["Body"].read()

        if len(image_bytes) > MAX_IMAGE_BYTES:
            logger.warning("Image exceeds limit: %s", key)
            continue

        try:
            resized_bytes = resize_image(image_bytes)
        except (UnidentifiedImageError, OSError, ValueError) as exc:
            logger.warning("Invalid image %s: %s", key, exc)
            continue

        relative_key = key[len("original/"):]
        output_key = (
                "processed/"
                + os.path.splitext(relative_key)[0]
                + ".jpg"
        )

        s3.put_object(
            Bucket=bucket,
            Key=output_key,
            Body=resized_bytes,
            ContentType="image/jpeg",
            CacheControl="public, max-age=86400"
        )

        processed_count += 1
        logger.info(
            "Successfully resized s3://%s/%s to s3://%s/%s",
            bucket, key, bucket, output_key
        )

    return {"processedCount": processed_count}
