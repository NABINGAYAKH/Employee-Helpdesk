package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.controller.AuthController;
import com.nabin.employee_helpdesk.dto.LoginRequest;
import com.nabin.employee_helpdesk.dto.RegisterRequest;
import com.nabin.employee_helpdesk.dto.UserResponse;
import com.nabin.employee_helpdesk.entity.UserRole;
import com.nabin.employee_helpdesk.service.AuthService;
import com.nabin.employee_helpdesk.service.CustomUserDetailsService;
import com.nabin.employee_helpdesk.service.JwtService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AuthService authService;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;


    @Test
    void shouldRegisterUser() throws Exception {

        // Arrange

        UserResponse response =
                new UserResponse(
                        1,
                        "nabin@gmail.com",
                        UserRole.EMPLOYEE
                );

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);


        // Act + Assert

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "nabin@gmail.com",
                                            "password": "password123"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("nabin@gmail.com"))
                .andExpect(jsonPath("$.role").value("EMPLOYEE"));
    }

    @Test
    void shouldLoginUser() throws Exception {

        // Arrange

        String token = "mock-jwt-token";

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(token);


        // Act + Assert

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "email": "nabin@gmail.com",
                                        "password": "password123"
                                    }
                                    """)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("mock-jwt-token"));
    }

    @Test
    void shouldRejectInvalidRegistration() throws Exception {

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "email": "invalid-email",
                                        "password": ""
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectInvalidLogin() throws Exception {

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "email": "invalid-email",
                                        "password": ""
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCallRegisterService() throws Exception {

        UserResponse response =
                new UserResponse(1, "nabin@gmail.com", UserRole.EMPLOYEE);

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "email": "nabin@gmail.com",
                                        "password": "password123"
                                    }
                                    """)
                )
                .andExpect(status().isOk());

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void shouldCallLoginService() throws Exception {

        when(authService.login(any(LoginRequest.class)))
                .thenReturn("mock-jwt-token");

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "email": "nabin@gmail.com",
                                        "password": "password123"
                                    }
                                    """)
                )
                .andExpect(status().isOk());

        verify(authService).login(any(LoginRequest.class));
    }
}