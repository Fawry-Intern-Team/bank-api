package com.example.bank_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InitiateTransactionResponse {
    private UUID transactionId;
    private String message;
}
