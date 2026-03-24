package com.hcl.feedbacksystem.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;

public record ApiError(
    @Schema(description = "Error timestamp", example = "2026-03-24T10:30:00")
        LocalDateTime timestamp,

    @Schema(description = "HTTP status code", example = "400")
        int status,

    @Schema(description = "HTTP status text", example = "Bad Request")
        String error,

    @Schema(description = "Human-readable error message", example = "Validation failed")
        String message,

    @Schema(description = "Request path", example = "/api/feedback")
        String path,

    @Schema(description = "Field level validation errors")
        Map<String, String> fieldErrors
) {

    public static ApiError of(HttpStatus httpStatus, String message, String path, Map<String, String> fieldErrors) {
        return new ApiError(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                path,
                fieldErrors == null ? Map.of() : fieldErrors
        );
    }
}
