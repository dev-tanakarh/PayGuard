package com.tanakarh.payguard.mapper.impl;

import org.springframework.stereotype.Component;

import com.tanakarh.payguard.domain.dto.request.TransactionDto;
import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;
import com.tanakarh.payguard.domain.entity.transaction.Transaction;
import com.tanakarh.payguard.mapper.TransactionMapper;

@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionResponseDto toResponseDto(Transaction transaction) {
              return new TransactionResponseDto(
                transaction.getId(),
                transaction.getTransactionReference(),
                transaction.getPayment().getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus(),
                transaction.getCreatedAt()
              );  
    }

    @Override
    public Transaction toEntity(TransactionDto transactionDto) {
        return Transaction.builder()
                            .amount(transactionDto.amount())
                            .type(transactionDto.type())
                            .currency(transactionDto.currency())
                            .build();
    }

}
