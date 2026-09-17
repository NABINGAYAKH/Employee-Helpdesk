package com.nabin.employee_helpdesk.controller;

import com.nabin.employee_helpdesk.dto.LoginRequest;
import com.nabin.employee_helpdesk.dto.RegisterRequest;
import com.nabin.employee_helpdesk.dto.UserResponse;
import com.nabin.employee_helpdesk.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "Authentication",
        description = "User registration and authentication APIs"
)
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;


    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with an EMPLOYEE role"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    @PostMapping("/api/auth/register")
    public UserResponse createRegister(@RequestBody @Valid RegisterRequest request){
        return authService.register(request);
    }




    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid login data"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/api/auth/login")
    public String login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }
}
