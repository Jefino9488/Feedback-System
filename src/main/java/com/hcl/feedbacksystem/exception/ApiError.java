package com.hcl.feedbacksystem.exception;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
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
