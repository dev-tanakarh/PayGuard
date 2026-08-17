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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntity'");
    }

    @Override
    public MerchantResponseDto toResponseDto(Merchant merchant) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toResponseDto'");
    }

}
