package com.tanakarh.payguard.domain.dto.response;

import java.time.Instant;

public record CustomerResponseDto(
    Long id,
    String firstName,
    String lastName,
    Instant createdAt,
    Instant updatedAt
) {

}
