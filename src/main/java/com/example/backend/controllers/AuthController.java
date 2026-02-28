package com.example.backend.controllers;

import com.example.backend.config.JwtService;
import com.example.backend.dto.security_dto.LoginRequest;
import com.example.backend.dto.security_dto.RegisterRequest;
import com.example.backend.entity.User;
import com.example.backend.service.security_service.LoginService;
import com.example.backend.service.security_service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;
    private final RegisterService registerService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          LoginService loginService,
                          RegisterService registerService,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.loginService = loginService;
        this.registerService = registerService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            User user = loginService.getUserByUsername(loginRequest.getUsername());
            String token = jwtService.generateToken(user.getUsername(), user.getRole());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "role", user.getRole(),
                    "fullName", user.getFullName()
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
    }

    @PostMapping("/reg")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        registerService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Пользователь " + registerRequest.getUsername() + " зарегистрирован");
    }
}