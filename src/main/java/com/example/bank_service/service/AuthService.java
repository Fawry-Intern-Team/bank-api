package com.example.bank_service.service;

import com.example.bank_service.dto.AuthRequest;
import com.example.bank_service.dto.AuthResponse;
import com.example.bank_service.dto.RegisterRequest;
import com.example.bank_service.dto.RegisterResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);

    RegisterResponse register(RegisterRequest request);
}
