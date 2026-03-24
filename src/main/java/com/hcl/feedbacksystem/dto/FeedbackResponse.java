package com.hcl.feedbacksystem.dto;

import com.hcl.feedbacksystem.entity.Feedback;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record FeedbackResponse(
    @Schema(description = "Feedback id", example = "10")
        Long id,

    @Schema(description = "Feedback rating", example = "5")
        Integer rating,

    @Schema(description = "Feedback comment", example = "Consistently delivers high-quality work.")
        String comment,

    @Schema(description = "Employee who gave the feedback")
        EmployeeResponse givenBy,

    @Schema(description = "Employee who received the feedback")
        EmployeeResponse givenTo,

    @Schema(description = "Creation timestamp in server timezone", example = "2026-03-24T10:30:00")
        LocalDateTime createdAt
) {

    public static FeedbackResponse fromEntity(Feedback feedback) {
        return new FeedbackResponse(
                feedback.getId(),
                feedback.getRating(),
                feedback.getComment(),
                EmployeeResponse.fromEntity(feedback.getGivenBy()),
                EmployeeResponse.fromEntity(feedback.getGivenTo()),
                feedback.getCreatedAt()
        );
    }
}
