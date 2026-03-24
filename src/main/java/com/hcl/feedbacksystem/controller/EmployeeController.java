package com.hcl.feedbacksystem.controller;

import com.hcl.feedbacksystem.dto.EmployeeResponse;
import com.hcl.feedbacksystem.exception.ApiError;
import com.hcl.feedbacksystem.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        @Operation(summary = "Get all employees", description = "Returns all registered employees sorted by id.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employees retrieved"),
            @ApiResponse(
                responseCode = "401",
                description = "Missing or invalid token",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            )
        })
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/me")
        @Operation(summary = "Get current authenticated employee", description = "Returns profile details for the logged-in user.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current employee retrieved"),
            @ApiResponse(
                responseCode = "401",
                description = "Missing or invalid token",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Authenticated employee not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            )
        })
    public ResponseEntity<EmployeeResponse> getCurrentEmployee() {
        return ResponseEntity.ok(employeeService.getCurrentEmployee());
    }

    @GetMapping("/{id}")
        @Operation(summary = "Get employee by id", description = "Returns a specific employee by numeric id.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee retrieved"),
            @ApiResponse(
                responseCode = "401",
                description = "Missing or invalid token",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Employee not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))
            )
        })
        public ResponseEntity<EmployeeResponse> getEmployeeById(
            @Parameter(description = "Employee id", example = "2")
            @PathVariable Long id
        ) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
}
