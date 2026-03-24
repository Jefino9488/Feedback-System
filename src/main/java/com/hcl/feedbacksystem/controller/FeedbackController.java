package com.hcl.feedbacksystem.controller;

import com.hcl.feedbacksystem.dto.FeedbackCreateRequest;
import com.hcl.feedbacksystem.dto.FeedbackResponse;
import com.hcl.feedbacksystem.exception.ApiError;
import com.hcl.feedbacksystem.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        @Operation(
            summary = "Submit feedback for another employee",
            description = "Creates feedback from the current authenticated employee to another employee."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Feedback submitted"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation failed or self-feedback attempt",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Missing or invalid token",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Target employee not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            )
        })
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.submitFeedback(request));
    }

    @GetMapping("/received")
        @Operation(summary = "Get feedback received by current employee", description = "Returns feedback items where the current employee is the recipient.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Received feedback retrieved"),
            @ApiResponse(
                responseCode = "401",
                description = "Missing or invalid token",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            )
        })
    public ResponseEntity<List<FeedbackResponse>> getReceivedFeedback() {
        return ResponseEntity.ok(feedbackService.getReceivedFeedback());
    }

    @GetMapping("/given")
        @Operation(summary = "Get feedback given by current employee", description = "Returns feedback items authored by the current employee.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Given feedback retrieved"),
            @ApiResponse(
                responseCode = "401",
                description = "Missing or invalid token",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            )
        })
    public ResponseEntity<List<FeedbackResponse>> getGivenFeedback() {
        return ResponseEntity.ok(feedbackService.getGivenFeedback());
    }
}
