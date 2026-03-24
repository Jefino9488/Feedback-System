package com.hcl.feedbacksystem.controller;

import com.hcl.feedbacksystem.dto.AuthResponse;
import com.hcl.feedbacksystem.dto.EmployeeResponse;
import com.hcl.feedbacksystem.dto.LoginRequest;
import com.hcl.feedbacksystem.dto.SignupRequest;
import com.hcl.feedbacksystem.exception.ApiError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hcl.feedbacksystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Signup and login endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
        @Operation(
            summary = "Create employee account",
            description = "Registers a new employee account using name, email, password, and department."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee account created"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation failed",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Email already registered",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            )
        })
    public ResponseEntity<EmployeeResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @PostMapping("/login")
        @Operation(
            summary = "Login and receive JWT token",
            description = "Authenticates an employee and returns a bearer token for authorized endpoints."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated successfully"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation failed",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Invalid credentials",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            )
        })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
