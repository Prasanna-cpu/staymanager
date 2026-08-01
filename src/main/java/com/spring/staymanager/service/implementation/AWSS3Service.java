package com.spring.staymanager.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AWSS3Service {

    @Value("${aws.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    private final S3Client s3Client;

    public String saveImageToRemoteBucket(MultipartFile photo){
        try{
            String fileName = photo.getOriginalFilename();
            String extension = "";

            if(fileName != null && fileName.contains(".")){
                extension = fileName.substring(fileName.lastIndexOf("."));
            }

            String generatedFileName = UUID.randomUUID() + extension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(generatedFileName)
                    .contentType(photo.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(photo.getInputStream(), photo.getSize())
            );

            return String.format(
                    "https://%s.s3.%s.amazonaws.com/%s",
                    bucketName,
                    region != null ? region : Region.AP_SOUTH_1.id(),
                    fileName
            );

        }
        catch(Exception e){
            log.error("Error while saving image to remote bucket", e);
            throw new RuntimeException("Error while saving image to remote bucket");
        }

    }

}
