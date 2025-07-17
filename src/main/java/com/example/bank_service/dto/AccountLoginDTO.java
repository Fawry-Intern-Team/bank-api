package com.example.bank_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class AccountLoginDTO {
    @NotBlank(message = "Card number is required")
    private String cardNumber;
    @NotBlank(message = "Password is required")
    private String password;
}
