package com.tanakarh.payguard.domain.dto.request;

public record CustomerDto(
    String firstName,
    String lastName,
    String email,
    String password
) {

}
