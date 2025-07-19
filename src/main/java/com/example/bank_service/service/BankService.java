package com.example.bank_service.service;

import com.example.bank_service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface BankService {
    TransactionResponseDTO deposit(TransactionRequestDTO dto);
    TransactionResponseDTO withdraw(TransactionRequestDTO dto);
    AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO);
    AccountResponseDTO getAccount(UUID id);
    AccountResponseDTO updateAccount(UUID id, AccountRequestDTO accountRequestDTO);
    void deleteAccount(UUID id);
    Page<AccountResponseDTO> getAllAccounts(Pageable pageable);
    Page<TransactionResponseDTO> getTransactionsForAccount(UUID id, Pageable pageable);

}
