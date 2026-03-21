package com.hcl.feedbacksystem.security;

import com.hcl.feedbacksystem.entity.Employee;
import com.hcl.feedbacksystem.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with email: " + username));

        return User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .roles("EMPLOYEE")
                .build();
    }
}
