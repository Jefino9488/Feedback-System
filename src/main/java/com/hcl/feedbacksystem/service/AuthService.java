package com.hcl.feedbacksystem.service;

import com.hcl.feedbacksystem.dto.AuthResponse;
import com.hcl.feedbacksystem.dto.EmployeeResponse;
import com.hcl.feedbacksystem.dto.LoginRequest;
import com.hcl.feedbacksystem.dto.SignupRequest;
import com.hcl.feedbacksystem.entity.Employee;
import com.hcl.feedbacksystem.repository.EmployeeRepository;
import com.hcl.feedbacksystem.security.JwtService;
import java.util.Locale;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public EmployeeResponse signup(SignupRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        if (employeeRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalStateException("Email is already registered");
        }

        Employee employee = Employee.builder()
                .name(request.name().trim())
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.password()))
                .department(request.department().trim())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeResponse.fromEntity(savedEmployee);
    }

    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.password())
        );

        Employee employee = employeeRepository.findByEmail(normalizedEmail)
            .orElseThrow(() -> new NoSuchElementException("Employee not found"));

        String token = jwtService.generateToken(employee.getEmail());
        return new AuthResponse(token, "Bearer", EmployeeResponse.fromEntity(employee));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
