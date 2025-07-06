package com.example.bank_service.controller;

import com.example.bank_service.dto.AccountDTO;
import com.example.bank_service.dto.TransactionDTO;
import com.example.bank_service.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@Slf4j
public class BankController {

    private final BankService bankService;

    @PostMapping
    public ResponseEntity<AccountDTO> create(@Valid @RequestBody AccountDTO dto) {
        return ResponseEntity.ok(bankService.createAccount(dto));
    }

    @GetMapping
    public Page<AccountDTO> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return bankService.getAllAccounts(PageRequest.of(page, size));
    }

    @GetMapping("/{cardNumber}")
    public AccountDTO get(@PathVariable String cardNumber) {
        return bankService.getAccount(cardNumber);
    }

    @PutMapping("/{cardNumber}")
    public AccountDTO update(@PathVariable String cardNumber, @RequestBody AccountDTO dto) {
        return bankService.updateAccount(cardNumber, dto);
    }

    @DeleteMapping("/{cardNumber}")
    public void delete(@PathVariable String cardNumber) {
        bankService.deleteAccount(cardNumber);
    }

    @PostMapping("/{cardNumber}/deposit")
    public void deposit(@PathVariable String cardNumber, @RequestParam BigDecimal amount) {
        log.info(cardNumber + amount);
        bankService.deposit(cardNumber, amount);
    }

    @PostMapping("/{cardNumber}/withdraw")
    public void withdraw(@PathVariable String cardNumber, @RequestParam BigDecimal amount) {
        bankService.withdraw(cardNumber, amount);
    }
    @GetMapping("/{cardNumber}/transactions")
    public Page<TransactionDTO> getTransactions(
            @PathVariable String cardNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return bankService.getTransactionsForAccount(cardNumber, PageRequest.of(page, size));
    }
}
