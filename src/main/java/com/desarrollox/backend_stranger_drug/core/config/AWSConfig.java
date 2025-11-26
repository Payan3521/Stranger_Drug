package com.desarrollox.backend_stranger_drug.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {
    private String accessKey;
    private String secretKey;
    private String region;

    @Bean
    public S3Client amazonS3Client() {

    }
    
}