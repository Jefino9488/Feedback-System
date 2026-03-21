package com.hcl.feedbacksystem.dto;

import com.hcl.feedbacksystem.entity.Employee;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
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
