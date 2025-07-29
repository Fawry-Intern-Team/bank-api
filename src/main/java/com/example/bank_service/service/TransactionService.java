package com.example.bank_service.service;

import com.example.bank_service.dto.ConfirmTransactionRequest;
import com.example.bank_service.dto.ConfirmTransactionResponse;
import com.example.bank_service.dto.InitiateTransactionRequest;
import com.example.bank_service.dto.InitiateTransactionResponse;

public interface TransactionService {
    InitiateTransactionResponse initiateTransaction(InitiateTransactionRequest request);
    ConfirmTransactionResponse confirmTransaction(ConfirmTransactionRequest request);
}
