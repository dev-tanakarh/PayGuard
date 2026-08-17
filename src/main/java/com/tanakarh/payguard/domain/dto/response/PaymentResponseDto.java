package com.tanakarh.payguard.domain.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponseDto(
    Long id,
    String paymentReference,
    Long customerId,
    Long merchantId,
    BigDecimal amount,
    String currency,
    String status,
    Instant createdAt,
    Instant updatedAt
) {
    
}
