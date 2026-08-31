package com.tanakarh.payguard.domain.dto.response;

import java.util.UUID;

public record AdminResponseDto(
    Long id,
    String name,
    String email
) {

}
