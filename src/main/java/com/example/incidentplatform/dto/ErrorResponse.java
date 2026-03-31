package com.example.incidentplatform.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    String code,
    String message,
    Instant timestamp,
    Map<String, String> fields
) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, Instant.now(), null);
    }

    public static ErrorResponse withFields(String code, String message, Map<String, String> fields) {
        return new ErrorResponse(code, message, Instant.now(), fields);
    }
}