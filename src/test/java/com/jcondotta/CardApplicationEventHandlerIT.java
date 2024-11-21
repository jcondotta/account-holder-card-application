package com.jcondotta;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.event.AccountHolderCreatedNotification;
import com.jcondotta.event.CardApplicationEventHandler;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import io.micronaut.context.ApplicationContext;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class CardApplicationEventHandlerIT implements LocalStackTestContainer {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();

    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();

    @Inject
    ApplicationContext applicationContext;

    @Inject
    JsonMapper jsonMapper;

    CardApplicationEventHandler cardApplicationEventHandler;

    @BeforeEach
    void beforeEach(){
        this.cardApplicationEventHandler = new CardApplicationEventHandler(applicationContext);
    }

    @Test
    void shouldProcessCardSuccessfully_whenValidSQSEventIsReceived() {
        var notification = new AccountHolderCreatedNotification(UUID.randomUUID(), ACCOUNT_HOLDER_NAME_JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);
        SNSEvent snsEvent = createSNSEvent(notification);

        cardApplicationEventHandler.execute(snsEvent);
    }

    private String serializeToJson(Object message) {
        try {
            return jsonMapper.writeValueAsString(message);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    private SNSEvent createSNSEvent(AccountHolderCreatedNotification accountHolderCreatedNotification) {
        var notification = serializeToJson(accountHolderCreatedNotification);

        SNSEvent.SNS sns = new SNSEvent.SNS();
        sns.setMessage(notification);

        SNSEvent.SNSRecord snsRecord = new SNSEvent.SNSRecord();
        snsRecord.setSns(sns);

        SNSEvent snsEvent = new SNSEvent();
        snsEvent.setRecords(Collections.singletonList(snsRecord));

        return snsEvent;
    }
}
