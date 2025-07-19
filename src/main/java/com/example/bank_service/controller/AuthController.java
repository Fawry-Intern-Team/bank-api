package com.example.bank_service.controller;

import com.example.bank_service.dto.AuthRequest;
import com.example.bank_service.dto.AuthResponse;
import com.example.bank_service.dto.RegisterRequest;
import com.example.bank_service.dto.RegisterResponse;
import com.example.bank_service.repository.UserRepository;
import com.example.bank_service.service.AuthServiceImpl;
import com.example.bank_service.service.JWTService;
import com.example.bank_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthServiceImpl authService;


    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}