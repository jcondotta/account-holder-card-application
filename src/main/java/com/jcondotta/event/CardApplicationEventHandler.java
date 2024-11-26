package com.jcondotta.event;

import com.jcondotta.configuration.CardApplicationSQSQueueConfig;
import com.jcondotta.event.request.AccountHolderCreatedNotification;
import com.jcondotta.event.request.CreateCardRequest;
import com.jcondotta.service.SerializationService;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;
import java.util.Map;

public class CardApplicationEventHandler extends MicronautRequestHandler<Map<String, Object>, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardApplicationEventHandler.class);

    @Inject
    private CardApplicationSQSQueueConfig sqsQueueConfig;

    @Inject
    private SerializationService serializationService;

    @Inject
    private SqsClient sqsClient;

    @Override
    public Void execute(Map<String, Object> event) {
        LOGGER.info("Received Event: {}", event);

        try {
            List<Map<String, Object>> records = (List<Map<String, Object>>) event.get("Records");
            if (records == null || records.isEmpty()) {
                LOGGER.error("No records found in event");
                return null;
            }

            for (Map<String, Object> record : records) {
                Map<String, Object> sns = (Map<String, Object>) record.get("Sns");
                if (sns == null) {
                    LOGGER.error("No Sns object found in record");
                    continue;
                }

                String message = (String) sns.get("Message");
                if (message == null) {
                    LOGGER.error("No Message found in Sns object");
                    continue;
                }

                LOGGER.info("Processing SNS message: {}", message);

                var notification = serializationService.deserialize(message, AccountHolderCreatedNotification.class);
                LOGGER.info("Deserialized Notification: {}", notification);

                MDC.put("bankAccountId", notification.bankAccountId().toString());
                MDC.put("accountHolderName", notification.accountHolderName());

                LOGGER.info("Received a card application event");

                var createCardRequest = new CreateCardRequest(notification.bankAccountId(), notification.accountHolderName());
                var serializedSQSMessage = serializationService.serialize(createCardRequest);

                var sendMessageRequest = SendMessageRequest.builder()
                        .queueUrl(sqsQueueConfig.queueURL())
                        .messageBody(serializedSQSMessage)
                        .build();

                sqsClient.sendMessage(sendMessageRequest);

                LOGGER.info("Successfully forwarded message to SQS queue '{}'", sqsQueueConfig.queueURL());
            }
        } catch (Exception e) {
            LOGGER.error("Error processing event", e.getMessage());
        }
        finally {
            MDC.clear();
        }
        return null;
    }
}
