package com.kaamaaya.awsoperations.configurations;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {

	@Value("${accessKey}")
	private String accessKey;
	
	@Value("${secretKey}")
	private String secretKey;

    @Value("${region}")
    private String region;

    @Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @SuppressWarnings("deprecation")
	@Bean
    public AmazonS3Client amazonS3Client(AWSCredentials awsCredentials) {
        @SuppressWarnings("deprecation")
		AmazonS3Client amazonS3Client = new AmazonS3Client(awsCredentials);
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
        return amazonS3Client;
    }
}
