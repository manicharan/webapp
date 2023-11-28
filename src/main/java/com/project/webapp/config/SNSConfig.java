package com.project.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Component
public class SNSConfig {
//    @Value("${aws.region}")
//    private String region;
    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(Region.of(Region.US_EAST_1.toString()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
