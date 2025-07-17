package com.example.bank_service.mapper;

import com.example.bank_service.dto.TransactionResponseDTO;
import com.example.bank_service.entity.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponseDTO toDto(Transaction transaction);
    List<TransactionResponseDTO> toDtoList(List<Transaction> transactions);
}
