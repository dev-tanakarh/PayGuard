package com.tanakarh.payguard.domain.dto.response;

import java.time.Instant;

import com.tanakarh.payguard.domain.entity.user.UserStatus;

public record CustomerResponseDto(
    Long id,
    String firstName,
    String lastName,
    UserStatus status,
    String email,
    Instant createdAt,
    Instant updatedAt
) {

}
