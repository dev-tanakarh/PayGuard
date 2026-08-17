package com.tanakarh.payguard.domain.dto.request;

import java.math.BigDecimal;

public record PaymentRequestDto(
    Long customerId,
    Long merchantId,
    BigDecimal amount,
    String currency
) {

}
