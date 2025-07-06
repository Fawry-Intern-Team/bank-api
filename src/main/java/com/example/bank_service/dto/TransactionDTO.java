package com.example.bank_service.dto;

import com.example.bank_service.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal amount;
    private TransactionType type;
    private String notes;
}
