package com.jcondotta.event;

import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;

@Serdeable
public record CreateCardRequest(UUID bankAccountId, String cardholderName) {
}
