package com.example.bank_service.dto;

import com.example.bank_service.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class InitiateTransactionRequest {
    @NotBlank(message = "idempotencyKey is required")
    private String idempotencyKey;
    @NotBlank(message = "cardNumber is required")
    private String cardNumber;
    @NotNull(message = "amount is required")
    private BigDecimal amount;
    @NotNull(message = "transactionType is required")
    private TransactionType transactionType;

}
