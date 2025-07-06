package com.example.bank_service.repository;


import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccount(Account account, Pageable pageable);


}
