package com.example.bank_service.service;

import com.example.bank_service.dto.*;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Transaction;
import com.example.bank_service.enums.TransactionType;
import com.example.bank_service.mapper.AccountMapper;
import com.example.bank_service.mapper.TransactionMapper;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    @Transactional
    @Override
    public TransactionResponseDTO deposit(TransactionRequestDTO dto) {
        Account account = accountRepository.findByCardNumber(dto.getCardNumber()).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(dto.getAmount()));
        Account saved = accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .notes("deposit")
                .type(TransactionType.DEPOSIT)
                .account(saved)
                .amount(dto.getAmount())
                .build();
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Transactional
    @Override
    public TransactionResponseDTO withdraw(TransactionRequestDTO dto) {
        Account account = accountRepository.findByCardNumber(dto.getCardNumber()).orElseThrow(() -> new RuntimeException("Account not found"));
        BigDecimal diff = account.getBalance().subtract(dto.getAmount());
        if (diff.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(dto.getAmount()));
        Account saved = accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .notes("withdraw")
                .type(TransactionType.DEPOSIT)
                .account(saved)
                .amount(dto.getAmount())
                .build();
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        Account account = accountMapper.toEntity(accountRequestDTO);
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    public AccountResponseDTO login(AccountLoginDTO dto) {
        Account account = accountRepository.findByCardNumber(dto.getCardNumber()).orElseThrow(() -> new RuntimeException("Account not found"));
        if (!account.getPassword().equals(dto.getPassword()))
            throw new RuntimeException("password invalid");
        return accountMapper.toDTO(account);
    }

    @Override
    public AccountResponseDTO getAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return accountMapper.toDTO(account);
    }

    @Override
    public AccountResponseDTO updateAccount(UUID id, AccountRequestDTO accountRequestDTO) {
        Account account = accountMapper.toEntity(accountRequestDTO);
        account.setId(id);
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.delete(account);
    }

    @Override
    public Page<AccountResponseDTO> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable).map(accountMapper::toDTO);
    }

    @Override
    public Page<TransactionResponseDTO> getTransactionsForAccount(UUID id, Pageable pageable) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccount(account, pageable).map(transactionMapper::toDto);
    }

}
