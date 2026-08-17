package com.tanakarh.payguard.mapper;

import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;
import com.tanakarh.payguard.domain.entity.transaction.Transaction;

public interface TransactionMapper {
    //Transaction toEntity(TransactionDto transaction);
    TransactionResponseDto toResponseDto(Transaction transaction);
}
