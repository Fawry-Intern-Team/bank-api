package com.example.bank_service.controller;

import com.example.bank_service.dto.*;
import com.example.bank_service.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@Slf4j
public class AccountController {

    private final AccountService bankService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.ok(bankService.createAccount(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/filter")
    public Page<AccountResponseDTO> filterAccounts(@RequestBody AccountFilterRequest request) {
        return bankService.filterAccounts(request, PageRequest.of(request.getPage(), request.getSize()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable UUID id) {
        return ResponseEntity.ok(bankService.getAccount(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public AccountResponseDTO update(@PathVariable UUID id, @Valid @RequestBody AccountUpdateDTO dto) {
        return bankService.updateAccount(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        bankService.deleteAccount(id);
    }


    @GetMapping("/{id}/transactions")
    public Page<TransactionResponseDTO> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @PathVariable UUID id) {
        return bankService.getTransactionsForAccount(id, PageRequest.of(page, size));
    }
}
