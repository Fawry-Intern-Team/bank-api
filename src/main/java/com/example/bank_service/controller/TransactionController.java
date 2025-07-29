package com.example.bank_service.controller;

import com.example.bank_service.dto.ConfirmTransactionRequest;
import com.example.bank_service.dto.ConfirmTransactionResponse;
import com.example.bank_service.dto.InitiateTransactionRequest;
import com.example.bank_service.dto.InitiateTransactionResponse;
import com.example.bank_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/initiate")
    public ResponseEntity<InitiateTransactionResponse> initiateTransaction(@Valid @RequestBody InitiateTransactionRequest request) {
        return ResponseEntity.ok(transactionService.initiateTransaction(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<ConfirmTransactionResponse> confirmTransaction(@Valid @RequestBody ConfirmTransactionRequest request) {

        return ResponseEntity.ok(transactionService.confirmTransaction(request));
    }
}
