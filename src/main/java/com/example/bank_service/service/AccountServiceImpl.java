package com.example.bank_service.service;

import com.example.bank_service.component.AccountSpecification;
import com.example.bank_service.dto.*;
import com.example.bank_service.entity.Account;
import com.example.bank_service.mapper.AccountMapper;
import com.example.bank_service.mapper.TransactionMapper;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        Account account = accountMapper.toEntity(accountRequestDTO);
        return accountMapper.toDTO(accountRepository.save(account));
    }


    @Override
    public AccountResponseDTO getAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return accountMapper.toDTO(account);
    }


    @Override
    public AccountResponseDTO updateAccount(UUID id, AccountUpdateDTO dto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setId(id);
        account.setCardName(dto.getCardName());
        account.setCardNumber(dto.getCardNumber());
        account.setEmail(dto.getEmail());
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.delete(account);
    }

    @Override
    public Page<AccountResponseDTO> filterAccounts(AccountFilterRequest request, Pageable pageable) {
        return accountRepository.findAll(
                AccountSpecification.filterBy(
                        request.getCardNumber(),
                        request.getCardName()
                ),
                pageable
        ).map(accountMapper::toDTO);
    }

    @Override
    public Page<TransactionResponseDTO> getTransactionsForAccount(UUID id, Pageable pageable) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccount(account, pageable).map(transactionMapper::toDto);
    }

}
