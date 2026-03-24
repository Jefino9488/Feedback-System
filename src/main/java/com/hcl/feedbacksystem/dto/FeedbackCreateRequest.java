package com.hcl.feedbacksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FeedbackCreateRequest(
        @Schema(description = "Feedback rating", example = "4", minimum = "1", maximum = "5")
        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be between 1 and 5")
        @Max(value = 5, message = "Rating must be between 1 and 5")
        Integer rating,

        @Schema(description = "Feedback comment", example = "Great collaboration and ownership in the sprint.")
        @NotBlank(message = "Comment is required")
        @Size(max = 1000, message = "Comment must not exceed 1000 characters")
        String comment,

        @Schema(description = "Recipient employee id", example = "2")
        @NotNull(message = "givenToEmployeeId is required")
        Long givenToEmployeeId
) {
}
