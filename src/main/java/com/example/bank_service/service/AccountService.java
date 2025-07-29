package com.example.bank_service.service;

import com.example.bank_service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AccountService {

    AccountResponseDTO createAccount(AccountRequestDTO dto);
    AccountResponseDTO getAccount(UUID id);
    AccountResponseDTO updateAccount(UUID id, AccountUpdateDTO dto);
    void deleteAccount(UUID id);
    Page<AccountResponseDTO> filterAccounts(AccountFilterRequest request, Pageable pageable);
    Page<TransactionResponseDTO> getTransactionsForAccount(UUID id, Pageable pageable);

}
