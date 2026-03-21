package com.hcl.feedbacksystem.service;

import com.hcl.feedbacksystem.dto.EmployeeResponse;
import com.hcl.feedbacksystem.entity.Employee;
import com.hcl.feedbacksystem.repository.EmployeeRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(EmployeeResponse::fromEntity)
                .toList();
    }

    public EmployeeResponse getEmployeeById(Long id) {
        return EmployeeResponse.fromEntity(getEmployeeEntityById(id));
    }

    public EmployeeResponse getCurrentEmployee() {
        return EmployeeResponse.fromEntity(getCurrentEmployeeEntity());
    }

    public Employee getCurrentEmployeeEntity() {
        String email = getAuthenticatedEmail();
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Authenticated employee not found"));
    }

    public Employee getEmployeeEntityById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + id));
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("Authentication required");
        }
        return authentication.getName();
    }
}
