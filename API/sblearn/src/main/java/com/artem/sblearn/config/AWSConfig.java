/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.artem.sblearn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 *
 * @author abdul.haseeb
 */
@Configuration
public class AWSConfig {
    
    @Value("${ACCESS_KEY_ID}")
    private String accessKeyId;

    @Value("${SECRET_KEY}")
    private String secretKey;
    
    @Value("${CLIENT_REGION}")
    private String awsRegion;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretKey);
        Region clientRegion = Region.of(awsRegion);
        
        return S3Client.builder()
                .region(clientRegion)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
    
}
