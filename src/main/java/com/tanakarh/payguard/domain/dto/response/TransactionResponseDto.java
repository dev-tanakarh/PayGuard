package com.tanakarh.payguard.domain.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.tanakarh.payguard.domain.entity.transaction.TransactionStatus;

import com.tanakarh.payguard.domain.entity.transaction.TransactionType;

public record TransactionResponseDto(
    Long id,
    String transactionReference,
    Long paymentId,
    TransactionType transactionType,
    BigDecimal amount,
    String currency,    
    TransactionStatus status,
    Instant createdAt
) {

}
