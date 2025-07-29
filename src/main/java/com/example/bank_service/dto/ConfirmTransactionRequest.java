package com.example.bank_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ConfirmTransactionRequest {
    @NotNull(message = "transactionId is required")
    private UUID transactionId;
    @NotBlank(message = "transactionId is required")
    private String otp;
}
