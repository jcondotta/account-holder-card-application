package com.jcondotta.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("aws.sqs.queues.card-application")
public record CardApplicationSQSQueueConfig(@NotBlank String queueURL) {

}