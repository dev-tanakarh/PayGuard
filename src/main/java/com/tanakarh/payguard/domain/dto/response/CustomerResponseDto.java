package com.tanakarh.payguard.domain.dto.response;

import java.time.Instant;

public record CustomerResponseDto(
    Long id,
    String firstName,
    String lastName,
    String email,
    String status,
    Instant createdAt,
    Instant updatedAt
) {

}
