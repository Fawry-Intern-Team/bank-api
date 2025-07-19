package com.example.bank_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private String cardNumber;

    private String cardName;

    private String username;
}
