package com.mohsinon.auth.controller;

import com.mohsinon.auth.dto.RegisterRequest;
import com.mohsinon.auth.dto.RegisterResponse;
import com.mohsinon.auth.dto.LoginRequest;
import com.mohsinon.auth.dto.LoginResponse;
import com.mohsinon.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
    
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

}