package com.hcl.feedbacksystem.controller;

import com.hcl.feedbacksystem.dto.FeedbackCreateRequest;
import com.hcl.feedbacksystem.dto.FeedbackResponse;
import com.hcl.feedbacksystem.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Feedback submission and retrieval")
@SecurityRequirement(name = "bearerAuth")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "Submit feedback for another employee")
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.submitFeedback(request));
    }

    @GetMapping("/received")
    @Operation(summary = "Get feedback received by current employee")
    public ResponseEntity<List<FeedbackResponse>> getReceivedFeedback() {
        return ResponseEntity.ok(feedbackService.getReceivedFeedback());
    }

    @GetMapping("/given")
    @Operation(summary = "Get feedback given by current employee")
    public ResponseEntity<List<FeedbackResponse>> getGivenFeedback() {
        return ResponseEntity.ok(feedbackService.getGivenFeedback());
    }
}
