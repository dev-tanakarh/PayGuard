package com.tanakarh.payguard.domain.dto.response;

import java.util.UUID;

public record AdminResponseDto(
    UUID id,
    String name,
    String email
) {

}
