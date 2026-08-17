package com.tanakarh.payguard.domain.dto.response;

import java.time.Instant;

public record MerchantResponseDto(
    Long id,
    String businessName,
    String businessEmail,
    String phone,
    String businessAddress,
    String registrationNumber,
    String status,
    Instant createdAt,
    Instant updatedAt
) {

}
