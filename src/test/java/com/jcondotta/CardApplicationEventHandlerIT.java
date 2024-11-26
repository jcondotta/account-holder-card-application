package com.jcondotta;

import com.jcondotta.container.LocalStackTestContainer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class CardApplicationEventHandlerIT implements LocalStackTestContainer {

//    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();
//
//    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();
//
//    @Inject
//    SqsClient sqsClient;
//
//    @Inject
//    SerializationService serializationService;
//
//    @Inject
//    ApplicationContext applicationContext;
//
//    @Inject
//    CardApplicationSQSQueueConfig sqsQueueConfig;
//
//    CardApplicationEventHandler cardApplicationEventHandler;
//
//    @BeforeEach
//    void beforeEach(){
//        this.cardApplicationEventHandler = new CardApplicationEventHandler(applicationContext);
//    }
//
//    @Test
//    void shouldPutMessageIntoSQSCardApplicationQueue_whenValidSNSEventIsReceived() {
//        var notification = new AccountHolderCreatedNotification(UUID.randomUUID(), ACCOUNT_HOLDER_NAME_JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);
//        SNSEvent snsEvent = createSNSEvent(notification);
//
//        cardApplicationEventHandler.execute(snsEvent);
//
//        var messages = sqsClient.receiveMessage(builder -> builder.queueUrl(sqsQueueConfig.queueURL())
//                        .waitTimeSeconds(3)
//                        .build())
//                .messages();
//
//        assertThat(messages)
//                .hasSize(1)
//                .first()
//                .satisfies(message -> {
//                    var createCardRequest = serializationService.deserialize(message.body(), CreateCardRequest.class);
//                    assertThat(createCardRequest.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL);
//                    assertThat(createCardRequest.cardholderName()).isEqualTo(ACCOUNT_HOLDER_NAME_JEFFERSON);
//                });
//    }
//
//    private SNSEvent createSNSEvent(AccountHolderCreatedNotification accountHolderCreatedNotification) {
//        var notification = serializationService.serialize(accountHolderCreatedNotification);
//
//        SNSEvent.SNS sns = new SNSEvent.SNS();
//        sns.setMessage(notification);
//
//        SNSEvent.SNSRecord snsRecord = new SNSEvent.SNSRecord();
//        snsRecord.setSns(sns);
//
//        SNSEvent snsEvent = new SNSEvent();
//        snsEvent.setRecords(Collections.singletonList(snsRecord));
//
//        return snsEvent;
//    }
}
