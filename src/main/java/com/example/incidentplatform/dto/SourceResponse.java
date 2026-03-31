package com.example.incidentplatform.dto;

import com.example.incidentplatform.domain.AlertSource;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record SourceResponse(
    UUID id,
    String name,
    String sourceType,
    String webhookUrl,
    String webhookSecret,
    boolean active,
    JsonNode transformConfig,
    Instant createdAt
) {
    public static SourceResponse from(AlertSource source, String baseUrl) {
        String webhookUrl = baseUrl + "/api/v1/webhooks/" + source.getId();
        return new SourceResponse(
            source.getId(),
            source.getName(),
            source.getSourceType(),
            webhookUrl,
            source.getWebhookSecret(),
            source.isActive(),
            source.getTransformConfig(),
            source.getCreatedAt()
        );
    }
}
