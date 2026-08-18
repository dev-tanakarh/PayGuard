package com.tanakarh.payguard.mapper.impl;

import org.springframework.stereotype.Component;

import com.tanakarh.payguard.domain.dto.request.MerchantDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;
import com.tanakarh.payguard.domain.entity.user.merchant.Merchant;
import com.tanakarh.payguard.mapper.MerchantMapper;

@Component
public class MerchantMapperImpl implements MerchantMapper {

    @Override
    public Merchant toEntity(MerchantDto merchantDto) {
        return Merchant.builder()
                        .businessName(merchantDto.businessName())
                        .businessAddress(merchantDto.businessAddress())
                        .registrationNumber(merchantDto.registrationNumber())
                        .phone(merchantDto.phone())
                        .build();
    }

    @Override
    public MerchantResponseDto toResponseDto(Merchant merchant) {
        return new MerchantResponseDto(
            merchant.getId(),
            merchant.getBusinessName(),
            merchant.getUser().getEmail(),
            merchant.getPhone(),
            merchant.getBusinessAddress(),
            merchant.getRegistrationNumber(),
            merchant.getUser().getStatus(),
            merchant.getCreatedAt(),
            merchant.getUpdatedAt()
        );
    }

}
