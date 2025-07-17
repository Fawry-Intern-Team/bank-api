package com.example.bank_service.repository;

import com.example.bank_service.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    @EntityGraph(attributePaths = "transactions")
    Page<Account> findAll(Pageable pageable);
    Optional<Account> findByCardNumber(String cardNumber);
}