package com.example.incidentplatform.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    private final String code;

    public NotFoundException(String entityType, UUID id) {
        super(entityType + " not found: " + id);
        this.code = entityType.toUpperCase() + "_NOT_FOUND";
    }

    // Usage: throw new NotFoundException("Incident", id)
    // Code:  "INCIDENT_NOT_FOUND"
}