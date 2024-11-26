package com.jcondotta.event.request;

import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;

@Serdeable
public record AccountHolderCreatedNotification(UUID accountHolderId, String accountHolderName, UUID bankAccountId) {}