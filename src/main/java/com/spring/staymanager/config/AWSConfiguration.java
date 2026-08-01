package com.spring.staymanager.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfiguration {

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.access.secret}")
    private String secretKey;

    @Value("${aws.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    @Bean
    public S3Client s3Client() {

        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(region != null ? Region.of(region) : Region.AP_SOUTH_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(credentials))
                .build();
    }


}
