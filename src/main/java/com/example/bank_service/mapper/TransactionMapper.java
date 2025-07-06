package com.example.bank_service.mapper;

import com.example.bank_service.dto.TransactionDTO;
import com.example.bank_service.entity.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDto(Transaction transaction);
    List<TransactionDTO> toDtoList(List<Transaction> transactions);
}
