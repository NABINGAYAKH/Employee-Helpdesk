package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.dto.LoginRequest;
import com.nabin.employee_helpdesk.dto.RegisterRequest;
import com.nabin.employee_helpdesk.dto.UserResponse;
import com.nabin.employee_helpdesk.entity.User;
import com.nabin.employee_helpdesk.entity.UserRole;
import com.nabin.employee_helpdesk.repository.UserRepository;
import com.nabin.employee_helpdesk.service.AuthService;
import com.nabin.employee_helpdesk.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthService authService;

    @Mock
    AuthenticationManager authenticationManager;


    @Mock
    JwtService jwtService;


    @Test
    void shouldRegisterUser(){

        //Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("nabin@gmail.com");
        request.setPassword("password123");

        User user = new User();
        user.setId(1);
        user.setEmail("nabin@gmail.com");
        user.setPassword("encodePassword");
        user.setRole(UserRole.EMPLOYEE);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodePassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        //Act
        UserResponse response = authService.register(request);

        //Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("nabin@gmail.com", response.getEmail());
        assertEquals(UserRole.EMPLOYEE, response.getRole());

        // Verify

        verify(passwordEncoder)
                .encode("password123");

        verify(userRepository)
                .save(any(User.class));

    }


    @Test
    void shouldSaveEncodedPassword() {

        // Arrange

        RegisterRequest request = new RegisterRequest();
        request.setEmail("nabin@gmail.com");
        request.setPassword("password123");

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // Act

        UserResponse response =
                authService.register(request);


        // Assert

        assertNotNull(response);

        // Verify

        verify(passwordEncoder)
                .encode("password123");

        verify(userRepository).save(
                argThat(user ->
                        user.getPassword().equals("encodedPassword")
                )
        );
    }

    @Test
    void shouldRegisterUserWithEmployeeRole() {

        // Arrange

        RegisterRequest request = new RegisterRequest();
        request.setEmail("nabin@gmail.com");
        request.setPassword("password123");

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act

        UserResponse response =
                authService.register(request);

        // Assert

        assertNotNull(response);
        assertEquals(UserRole.EMPLOYEE, response.getRole());

        // Verify

        verify(userRepository).save(
                argThat(user ->
                        user.getRole() == UserRole.EMPLOYEE
                )
        );
    }

    @Test
    void shouldLoginSuccessfully() {

        // Arrange

        LoginRequest request = new LoginRequest();
        request.setEmail("nabin@gmail.com");
        request.setPassword("password123");

        when(jwtService.generateToken("nabin@gmail.com"))
                .thenReturn("mock-jwt-token");

        // Act

        String token = authService.login(request);

        // Assert

        assertNotNull(token);
        assertEquals("mock-jwt-token", token);

        // Verify

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        verify(jwtService)
                .generateToken("nabin@gmail.com");
    }

    @Test
    void shouldThrowExceptionWhenLoginAuthenticationFails() {

        // Arrange

        LoginRequest request = new LoginRequest();
        request.setEmail("nabin@gmail.com");
        request.setPassword("wrongPassword");

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act + Assert

        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        // Verify

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        verify(jwtService, never())
                .generateToken(anyString());
    }

    @Test
    void shouldAuthenticateWithCorrectCredentials() {

        // Arrange

        LoginRequest request = new LoginRequest();
        request.setEmail("nabin@gmail.com");
        request.setPassword("password123");

        when(jwtService.generateToken("nabin@gmail.com"))
                .thenReturn("mock-jwt-token");

        // Act

        authService.login(request);

        // Verify

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        "nabin@gmail.com",
                        "password123"
                )
        );

        verify(jwtService)
                .generateToken("nabin@gmail.com");
    }

    @Test
    void shouldThrowExceptionWhenJwtGenerationFails() {

        // Arrange

        LoginRequest request = new LoginRequest();
        request.setEmail("nabin@gmail.com");
        request.setPassword("password123");

        when(jwtService.generateToken("nabin@gmail.com"))
                .thenThrow(new RuntimeException("JWT generation failed"));

        // Act + Assert

        assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        // Verify

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        verify(jwtService)
                .generateToken("nabin@gmail.com");
    }

}
