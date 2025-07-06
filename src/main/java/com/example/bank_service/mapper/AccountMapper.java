package com.example.bank_service.mapper;

import com.example.bank_service.dto.AccountDTO;
import com.example.bank_service.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDTO dto);
    AccountDTO toDTO(Account entity);
}
