package com.tanakarh.payguard.mapper;

import com.tanakarh.payguard.domain.dto.request.MerchantDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;
import com.tanakarh.payguard.domain.entity.user.merchant.Merchant;

public interface MerchantMapper {
    Merchant toEntity(MerchantDto merchantDto);
    MerchantResponseDto toResponseDto(Merchant merchant);
}
