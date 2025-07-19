package com.example.bank_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private UUID id;
    private String cardNumber;
    private String cardName;
    private BigDecimal balance;
    private List<TransactionResponseDTO> transactions;
}
