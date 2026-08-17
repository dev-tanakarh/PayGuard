package com.tanakarh.payguard.domain.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponseDto(
    Long id,
    String transactionReference,
    Long paymentId,
    String transactionType,
    BigDecimal amount,
    String currency,    
    String status,
    Instant createdAt
) {

}
