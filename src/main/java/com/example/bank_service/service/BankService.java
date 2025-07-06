package com.example.bank_service.service;

import com.example.bank_service.dto.AccountDTO;
import com.example.bank_service.dto.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface BankService {
    void deposit(String cardNumber, BigDecimal amount);
    void withdraw(String cardNumber, BigDecimal amount);
    void debit(String cardNumber, BigDecimal amount);   // for OrderService
    void credit(String cardNumber, BigDecimal amount);  // for rollback
    AccountDTO createAccount(AccountDTO accountDTO);
    AccountDTO getAccount(String cardNumber);
    AccountDTO updateAccount(String cardNumber, AccountDTO accountDTO);
    void deleteAccount(String cardNumber);
    Page<AccountDTO> getAllAccounts(Pageable pageable);
    Page<TransactionDTO> getTransactionsForAccount(String cardNumber, Pageable pageable);

}
