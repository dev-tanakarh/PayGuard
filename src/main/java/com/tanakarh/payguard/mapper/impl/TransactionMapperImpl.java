package com.tanakarh.payguard.mapper.impl;

import org.springframework.stereotype.Component;

import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;
import com.tanakarh.payguard.mapper.TransactionMapper;

@Component
public class TransactionMapperImpl implements TransactionMapper {

    // @Override
    // public TransactionResponseDto toResponseDto(Transaction transaction) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'toResponseDto'");
    // }

    @Override
    public TransactionResponseDto toResponseDto(
            com.tanakarh.payguard.domain.entity.transaction.Transaction transaction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toResponseDto'");
    }

}
