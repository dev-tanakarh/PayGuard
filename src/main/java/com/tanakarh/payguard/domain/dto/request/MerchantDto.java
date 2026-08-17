package com.tanakarh.payguard.domain.dto.request;

public record MerchantDto(
    String businessName,
    String businessEmail,
    String phone,
    String businessAddress,
    String registrationNumber

) {

}
