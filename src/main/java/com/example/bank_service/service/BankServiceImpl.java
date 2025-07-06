package com.example.bank_service.service;

import com.example.bank_service.dto.AccountDTO;
import com.example.bank_service.dto.TransactionDTO;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;


    @Override
    public AccountDTO createAccount(AccountDTO dto) {
        Account account = accountMapper.toEntity(dto);
        account.setBalance(BigDecimal.ZERO); // default
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO getAccount(String cardNumber) {
        return accountMapper.toDTO(getAccountOrThrow(cardNumber));
    }

    @Override
    public AccountDTO updateAccount(String cardNumber, AccountDTO dto) {
        Account acc = getAccountOrThrow(cardNumber);
        acc.setName(dto.getName());
        acc.setPassword(dto.getPassword());
        return accountMapper.toDTO(accountRepository.save(acc));
    }

    @Override
    public void deleteAccount(String cardNumber) {
        accountRepository.deleteById(cardNumber);
    }

    @Override
    public Page<AccountDTO> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable).map(accountMapper::toDTO);
    }

    @Override
    public void deposit(String cardNumber, BigDecimal amount) {
        Account acc = getAccountOrThrow(cardNumber);
        acc.setBalance(acc.getBalance().add(amount));
        accountRepository.save(acc);

        saveTransaction(acc, amount, TransactionType.DEPOSIT, "Deposit");
    }

    @Override
    public void withdraw(String cardNumber, BigDecimal amount) {
        Account acc = getAccountOrThrow(cardNumber);
        if (acc.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        acc.setBalance(acc.getBalance().subtract(amount));
        accountRepository.save(acc);

        saveTransaction(acc, amount, TransactionType.WITHDRAWAL, "Withdraw");
    }

    @Override
    public void debit(String cardNumber, BigDecimal amount) {
        withdraw(cardNumber, amount);
    }

    @Override
    public void credit(String cardNumber, BigDecimal amount) {
        deposit(cardNumber, amount);
    }
    @Override
    public Page<TransactionDTO> getTransactionsForAccount(String cardNumber, Pageable pageable) {
        Account account = getAccountOrThrow(cardNumber);
        return transactionRepository.findByAccount(account, pageable)
                .map(transactionMapper::toDto);
    }
    private void saveTransaction(Account acc, BigDecimal amount, TransactionType type, String note) {
        transactionRepository.save(Transaction.builder()
                .account(acc)
                .amount(amount)
                .type(type)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .notes(note)
                .build());
    }

    private Account getAccountOrThrow(String cardNumber) {
        return accountRepository.findById(cardNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
