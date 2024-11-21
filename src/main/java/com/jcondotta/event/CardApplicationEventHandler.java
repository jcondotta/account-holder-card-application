package com.jcondotta.event;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.jcondotta.configuration.CardApplicationSQSQueueConfig;
import com.jcondotta.service.SerializationService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class CardApplicationEventHandler extends MicronautRequestHandler<SNSEvent, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardApplicationEventHandler.class);

    @Inject
    private SqsClient sqsClient;

    @Inject
    private CardApplicationSQSQueueConfig sqsQueueConfig;

    @Inject
    private SerializationService serializationService;

    public CardApplicationEventHandler() {
        super();
    }

    public CardApplicationEventHandler(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public Void execute(SNSEvent snsEvent) {
        for (SNSEvent.SNSRecord record : snsEvent.getRecords()) {
            try {
                var snsMessage = record.getSNS().getMessage();
                var notification = serializationService.deserialize(snsMessage, AccountHolderCreatedNotification.class);

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
            } catch (Exception e) {
//                logger.error("Unexpected error while processing SNS message (MessageId={}): {}",
//                        record.getSNS().getMessageId(), e.getMessage(), e);
                throw new RuntimeException("Unexpected error processing SNS message with MessageId: " + record.getSNS().getMessageId(), e);
            }
            finally {
                MDC.clear();
            }
        }
        return null;
    }
}
