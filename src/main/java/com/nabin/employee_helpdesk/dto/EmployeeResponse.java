package com.nabin.employee_helpdesk.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

    private Integer id;
    private String name;
    private String email;
    private String department;
}
