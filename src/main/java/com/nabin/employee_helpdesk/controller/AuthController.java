package com.nabin.employee_helpdesk.controller;

import com.nabin.employee_helpdesk.dto.LoginRequest;
import com.nabin.employee_helpdesk.dto.RegisterRequest;
import com.nabin.employee_helpdesk.dto.UserResponse;
import com.nabin.employee_helpdesk.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/api/auth/register")
    public UserResponse createRegister(@RequestBody @Valid RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/api/auth/login")
    public String login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }
}
