package com.example.bank_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequestDTO {
    @NotBlank(message = "Card number is required")
    private String cardNumber;
    @NotBlank(message = "Name is required")
    private String cardName;
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    private BigDecimal balance;
}
