package com.example.bank_service.controller;

import com.example.bank_service.dto.*;
import com.example.bank_service.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@Slf4j
public class BankController {

    private final BankService bankService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.ok(bankService.createAccount(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<AccountResponseDTO> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return bankService.getAllAccounts(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable UUID id) {
        return ResponseEntity.ok(bankService.getAccount(id));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<AccountResponseDTO> getAccountByUsername(@PathVariable String username) {
        return ResponseEntity.ok(bankService.getAccountByUsername(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public AccountResponseDTO update(@PathVariable UUID id, @RequestBody AccountRequestDTO dto) {
        return bankService.updateAccount(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        bankService.deleteAccount(id);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@Valid @RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(bankService.deposit(dto));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(@Valid @RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(bankService.withdraw(dto));
    }

    @GetMapping("/{id}/transactions")
    public Page<TransactionResponseDTO> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @PathVariable UUID id) {
        return bankService.getTransactionsForAccount(id, PageRequest.of(page, size));
    }
}
