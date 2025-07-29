package com.example.bank_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class ConfirmTransactionResponse {
    private UUID transactionId;
}
