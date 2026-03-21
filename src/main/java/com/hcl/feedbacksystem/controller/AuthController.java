package com.hcl.feedbacksystem.controller;

import com.hcl.feedbacksystem.dto.AuthResponse;
import com.hcl.feedbacksystem.dto.EmployeeResponse;
import com.hcl.feedbacksystem.dto.LoginRequest;
import com.hcl.feedbacksystem.dto.SignupRequest;
import com.hcl.feedbacksystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Create employee account")
    public ResponseEntity<EmployeeResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and receive JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
