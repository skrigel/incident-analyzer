package com.example.incidentplatform.transformer;

import com.example.incidentplatform.domain.AlertSource;
import com.example.incidentplatform.dto.AlertRequest;
import com.fasterxml.jackson.databind.JsonNode;

public interface AlertTransformer {

    /**
     * Transforms external webhook payload into AlertRequest
     * @param payload Raw JSON from external system
     * @param source The registered AlertSource with transform config
     * @return AlertRequest ready for ingestion
     */
    AlertRequest transform(JsonNode payload, AlertSource source);

    /**
     * Validates if this transformer can handle the given source type
     */
    boolean supports(String sourceType);
}