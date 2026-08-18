package com.tanakarh.payguard.domain.dto.response;

import java.time.Instant;

import com.tanakarh.payguard.domain.entity.user.merchant.MerchantStatus;

public record MerchantResponseDto(
    Long id,
    String businessName,
    String businessEmail,
    String phone,
    String businessAddress,
    String registrationNumber,
    MerchantStatus status,
    Instant createdAt,
    Instant updatedAt
) {

}
