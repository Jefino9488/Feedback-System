package com.hcl.feedbacksystem.service;

import com.hcl.feedbacksystem.dto.FeedbackCreateRequest;
import com.hcl.feedbacksystem.dto.FeedbackResponse;
import com.hcl.feedbacksystem.entity.Employee;
import com.hcl.feedbacksystem.entity.Feedback;
import com.hcl.feedbacksystem.repository.EmployeeRepository;
import com.hcl.feedbacksystem.repository.FeedbackRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    public FeedbackResponse submitFeedback(FeedbackCreateRequest request) {
        Employee givenBy = employeeService.getCurrentEmployeeEntity();
        Employee givenTo = employeeRepository.findById(request.givenToEmployeeId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + request.givenToEmployeeId()));

        if (givenBy.getId().equals(givenTo.getId())) {
            throw new IllegalArgumentException("Cannot give feedback to yourself");
        }

        Feedback feedback = Feedback.builder()
                .rating(request.rating())
                .comment(request.comment().trim())
                .givenBy(givenBy)
                .givenTo(givenTo)
                .createdAt(LocalDateTime.now())
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return FeedbackResponse.fromEntity(savedFeedback);
    }

    public List<FeedbackResponse> getReceivedFeedback() {
        Employee currentEmployee = employeeService.getCurrentEmployeeEntity();
        return feedbackRepository.findByGivenToIdOrderByCreatedAtDesc(currentEmployee.getId())
                .stream()
                .map(FeedbackResponse::fromEntity)
                .toList();
    }

    public List<FeedbackResponse> getGivenFeedback() {
        Employee currentEmployee = employeeService.getCurrentEmployeeEntity();
        return feedbackRepository.findByGivenByIdOrderByCreatedAtDesc(currentEmployee.getId())
                .stream()
                .map(FeedbackResponse::fromEntity)
                .toList();
    }
}
