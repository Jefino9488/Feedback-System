package com.hcl.feedbacksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "Employee email address", example = "alex.johnson@company.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        String email,

        @Schema(description = "Account password", example = "Password@123")
        @NotBlank(message = "Password is required")
        String password
) {
}
