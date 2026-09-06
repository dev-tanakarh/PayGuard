package com.tanakarh.payguard.domain.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record PaymentRequestDto(
    @NotNull(message = "Customer ID required")
    @Positive(message = "Customer ID must be positive")
    Long customerId,

    @NotNull(message = "Merchant ID required")
    @Positive(message = "Merchant ID must be positive")
    Long merchantId,

    @NotNull(message = "Amount required")
    @Positive(message = "Amount must be positive and greater than zero")
    @DecimalMax(value = "999999.99", message = "Amount exceeds maximum")
    BigDecimal amount,

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3-letter code")
    String currency
) {

}
