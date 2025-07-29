package com.example.bank_service.mapper;

import com.example.bank_service.dto.AccountRequestDTO;
import com.example.bank_service.dto.AccountResponseDTO;
import com.example.bank_service.dto.AccountUpdateDTO;
import com.example.bank_service.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountRequestDTO dto);
    AccountResponseDTO toDTO(Account entity);
}
