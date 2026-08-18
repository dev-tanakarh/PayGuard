package com.tanakarh.payguard.domain.dto.response;

import java.time.Instant;

import com.tanakarh.payguard.domain.entity.user.UserStatus;

public record MerchantResponseDto(
    Long id,
    String businessName,
    String businessEmail,
    String phone,
    String businessAddress,
    String registrationNumber,
    UserStatus status,
    Instant createdAt,
    Instant updatedAt
) {

}
