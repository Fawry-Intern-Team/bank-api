package com.example.bank_service.service;

import com.example.bank_service.dto.ConfirmTransactionRequest;
import com.example.bank_service.dto.ConfirmTransactionResponse;
import com.example.bank_service.dto.InitiateTransactionRequest;
import com.example.bank_service.dto.InitiateTransactionResponse;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Transaction;
import com.example.bank_service.enums.TransactionStatus;
import com.example.bank_service.enums.TransactionType;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.TransactionRepository;
import com.example.bank_service.util.OTPUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.NotificationEvent;
import org.example.events.NotificationType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(5);
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public InitiateTransactionResponse initiateTransaction(InitiateTransactionRequest request) {
        Account account = accountRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Optional<Transaction> optionalTransaction = transactionRepository.findByIdempotencyKey(request.getIdempotencyKey());
        Transaction transaction = null;
        if (optionalTransaction.isPresent()) {
            transaction = optionalTransaction.get();
        }

        if (optionalTransaction.isEmpty()) {
            String opt = OTPUtil.generateOTP();
            transaction = Transaction
                    .builder()
                    .account(account)
                    .idempotencyKey(request.getIdempotencyKey())
                    .amount(request.getAmount())
                    .type(request.getTransactionType())
                    .status(TransactionStatus.PENDING)
                    .otp(opt)
                    .expiresAt(LocalDateTime.now().plus(Duration.ofMillis(VALIDITY)))
                    .build();
            return getInitiateTransactionResponse(account, transaction, opt,"transaction created successfully");
        }
        if (transaction.getExpiresAt().isBefore(LocalDateTime.now())) {
            String opt = OTPUtil.generateOTP();
            transaction.setOtp(opt);
            transaction.setExpiresAt(LocalDateTime.now().plus(Duration.ofMillis(VALIDITY)));
            return getInitiateTransactionResponse(account, transaction, opt,"OTP expired and new one sent");
        }
        return InitiateTransactionResponse
                .builder()
                .transactionId(transaction.getTransactionId())
                .message("transaction already exist")
                .build();
    }

    private InitiateTransactionResponse getInitiateTransactionResponse(Account account, Transaction transaction, String opt,String message) {
        Transaction savedTransaction=  transactionRepository.save(transaction);
        NotificationEvent event = NotificationEvent
                .builder()
                .subject("OTP Confirmation")
                .content("Your OTP is : " + opt + " .it will expire in 5 minutes.")
                .recipient(account.getEmail())
                .type(NotificationType.EMAIL)
                .build();
        rabbitTemplate.convertAndSend("notification-queue", event);

        return InitiateTransactionResponse
                .builder()
                .transactionId(savedTransaction.getTransactionId())
                .message(message)
                .build();
    }

    @Override
    public ConfirmTransactionResponse confirmTransaction(ConfirmTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId()).orElseThrow(() -> new RuntimeException("Transaction id not found"));
        if (!transaction.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("OTP not match");
        }
        if (transaction.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }
        if (transaction.getStatus().equals(TransactionStatus.COMPLETED)) {
            throw new RuntimeException("Transaction completed");
        }
        if (transaction.getType().equals(TransactionType.DEPOSIT)) {
            deposit(transaction);
        } else {
            withdraw(transaction);
        }
        transaction.setStatus(TransactionStatus.COMPLETED);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return ConfirmTransactionResponse
                .builder()
                .transactionId(savedTransaction.getTransactionId())
                .build();
    }

    private void deposit(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(transaction.getAmount()));
        accountRepository.save(account);
    }

    private void withdraw(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance().subtract(transaction.getAmount()).compareTo(transaction.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        accountRepository.save(account);
    }
}
