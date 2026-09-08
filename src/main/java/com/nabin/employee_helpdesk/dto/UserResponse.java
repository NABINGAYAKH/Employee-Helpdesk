package com.nabin.employee_helpdesk.dto;

import com.nabin.employee_helpdesk.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Integer id;
    private String email;
    private UserRole role;
}