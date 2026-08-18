package com.tanakarh.payguard.domain.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.tanakarh.payguard.domain.entity.payment.PaymentStatus;

public record PaymentResponseDto(
    Long id,
    String paymentReference,
    Long customerId,
    Long merchantId,
    BigDecimal amount,
    String currency,
    PaymentStatus status,
    Instant createdAt,
    Instant updatedAt
) {
    
}
