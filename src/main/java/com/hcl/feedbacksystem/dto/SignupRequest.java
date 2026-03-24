package com.hcl.feedbacksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @Schema(description = "Employee full name", example = "Alex Johnson")
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Employee email address", example = "alex.johnson@company.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        String email,

        @Schema(description = "Account password (minimum 8 characters)", example = "Password@123")
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,

        @Schema(description = "Employee department", example = "Engineering")
        @NotBlank(message = "Department is required")
        String department
) {
}
