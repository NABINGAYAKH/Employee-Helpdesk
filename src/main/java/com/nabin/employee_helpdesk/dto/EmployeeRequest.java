package com.nabin.employee_helpdesk.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String department;

}
