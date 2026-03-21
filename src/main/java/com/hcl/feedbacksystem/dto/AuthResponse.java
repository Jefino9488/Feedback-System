package com.hcl.feedbacksystem.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        EmployeeResponse employee
) {
}
