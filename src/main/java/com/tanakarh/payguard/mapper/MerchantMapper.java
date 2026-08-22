package com.tanakarh.payguard.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.tanakarh.payguard.domain.dto.request.MerchantDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;
import com.tanakarh.payguard.domain.entity.user.merchant.Merchant;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MerchantMapper {

    Merchant toEntity(MerchantDto merchantDto);

    @Mapping(source = "user.email", target = "businessEmail")
    @Mapping(source = "user.status", target = "status")
    MerchantResponseDto toResponseDto(Merchant merchant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateMerchantFromDto(MerchantDto merchantDto, @MappingTarget Merchant merchant);
}
