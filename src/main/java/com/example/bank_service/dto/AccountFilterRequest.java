package com.example.bank_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountFilterRequest {
    private String cardNumber;
    private String cardName;
    private int size;
    private int page;
}
