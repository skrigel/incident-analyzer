package com.example.incidentplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email
        ) {
}
