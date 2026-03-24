package com.hcl.feedbacksystem.dto;

import com.hcl.feedbacksystem.entity.Employee;
import io.swagger.v3.oas.annotations.media.Schema;

public record EmployeeResponse(
    @Schema(description = "Employee id", example = "1")
        Long id,

    @Schema(description = "Employee full name", example = "Alex Johnson")
        String name,

    @Schema(description = "Employee email address", example = "alex.johnson@company.com")
        String email,

    @Schema(description = "Employee department", example = "Engineering")
        String department
) {

    public static EmployeeResponse fromEntity(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment()
        );
    }
}
