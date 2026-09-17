package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private String secret;

    @BeforeEach
    void setUp() {

        jwtService = new JwtService();

        secret = Base64.getEncoder().encodeToString(
                "my-super-secret-key-for-testing-jwt-123456789"
                        .getBytes()
        );

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                secret
        );
    }


    @Test
    void shouldGenerateToken() {

        // Act

        String token =
                jwtService.generateToken("nabin@gmail.com");

        // Assert

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractUsernameFromToken() {

        // Arrange

        String username = "nabin@gmail.com";

        String token =
                jwtService.generateToken(username);

        // Act

        String extractedUsername =
                jwtService.extractUsername(token);

        // Assert

        assertNotNull(extractedUsername);
        assertEquals(username, extractedUsername);
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {

        // Arrange

        String invalidToken = "this-is-not-a-valid-jwt";

        // Act + Assert

        assertThrows(
                Exception.class,
                () -> jwtService.extractUsername(invalidToken)
        );
    }

    @Test
    void shouldContainCorrectUsernameInToken() {

        // Arrange

        String username = "nabin@gmail.com";

        // Act

        String token = jwtService.generateToken(username);

        String extractedUsername =
                jwtService.extractUsername(token);

        // Assert

        assertEquals(
                "nabin@gmail.com",
                extractedUsername
        );
    }
}