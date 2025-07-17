package com.example.bank_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionRequestDTO {
    @NotBlank(message = "cardNumber are required")
    private String cardNumber;
    @NotNull(message = "amount are required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Amount must be at least 0.1")
    private BigDecimal amount;

}
