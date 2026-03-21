package com.hcl.feedbacksystem.dto;

import com.hcl.feedbacksystem.entity.Feedback;
import java.time.LocalDateTime;

public record FeedbackResponse(
        Long id,
        Integer rating,
        String comment,
        EmployeeResponse givenBy,
        EmployeeResponse givenTo,
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
