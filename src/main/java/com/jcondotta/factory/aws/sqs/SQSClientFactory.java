package com.jcondotta.factory.aws.sqs;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Factory
public class SQSClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQSClientFactory.class);

    @Singleton
    @Replaces(SqsClient.class)
    @Requires(missingProperty = "aws.sqs.endpoint")
    public SqsClient sqsClient(Region region){
        var environmentVariableCredentialsProvider = EnvironmentVariableCredentialsProvider.create();
        var awsCredentials = environmentVariableCredentialsProvider.resolveCredentials();

        LOGGER.info("Building SQSClient with params: awsCredentials: {} and region: {}", awsCredentials, region);

        return SqsClient.builder()
                .region(region)
                .credentialsProvider(environmentVariableCredentialsProvider)
                .build();
    }

    @Singleton
    @Replaces(SqsClient.class)
    @Requires(property = "aws.sqs.endpoint")
    public SqsClient sqsClientEndpointOverridden(AwsCredentials awsCredentials, Region region, @Value("${aws.sqs.endpoint}") String sqsEndpoint){
        LOGGER.info("Building SQSClient with params: awsCredentials: {}, region: {} and endpoint: {}", awsCredentials, region, sqsEndpoint);

        return SqsClient.builder()
                .region(region)
                .endpointOverride(URI.create(sqsEndpoint))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}