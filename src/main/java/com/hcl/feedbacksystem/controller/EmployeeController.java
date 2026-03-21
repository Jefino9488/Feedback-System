package com.hcl.feedbacksystem.controller;

import com.hcl.feedbacksystem.dto.EmployeeResponse;
import com.hcl.feedbacksystem.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Employee lookup endpoints")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @Operation(summary = "Get all employees")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated employee")
    public ResponseEntity<EmployeeResponse> getCurrentEmployee() {
        return ResponseEntity.ok(employeeService.getCurrentEmployee());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by id")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
}
