package com.example.bank_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Card number is required")
    private String cardNumber;
    @NotBlank(message = "Name is required")
    private String cardName;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "username is required")
    private String username;
}