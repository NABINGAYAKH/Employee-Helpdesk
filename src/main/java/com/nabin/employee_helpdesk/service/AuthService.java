package com.nabin.employee_helpdesk.service;

import com.nabin.employee_helpdesk.dto.LoginRequest;
import com.nabin.employee_helpdesk.dto.RegisterRequest;
import com.nabin.employee_helpdesk.dto.UserResponse;
import com.nabin.employee_helpdesk.entity.User;
import com.nabin.employee_helpdesk.entity.UserRole;
import com.nabin.employee_helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public UserResponse register(RegisterRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        String encodedPassword=
                passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(UserRole.EMPLOYEE);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public String login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        return jwtService.generateToken(request.getEmail());
    }
}
