package com.tanakarh.payguard.domain.dto.response;

import java.time.Instant;

import com.tanakarh.payguard.domain.entity.user.customer.CustomerStatus;

public record CustomerResponseDto(
    Long id,
    String firstName,
    String lastName,
    String email,
    CustomerStatus status,
    Instant createdAt,
    Instant updatedAt
) {

}
