package com.example.incidentplatform.dto;

import java.util.UUID;

public record WebhookResponse(
    UUID alertId,
    UUID incidentId,
    String status
) {
    public static WebhookResponse from(AlertResponse alertResponse) {
        return new WebhookResponse(
            alertResponse.id(),
            alertResponse.incidentId(),
            "processed"
        );
    }
}