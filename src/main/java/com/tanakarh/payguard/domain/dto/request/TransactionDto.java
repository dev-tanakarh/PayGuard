package com.tanakarh.payguard.domain.dto.request;

import java.math.BigDecimal;

import com.tanakarh.payguard.domain.entity.transaction.TransactionType;

public record TransactionDto(
    BigDecimal amount,
    TransactionType type,
    Long paymentId,
    String currency

) {

}
