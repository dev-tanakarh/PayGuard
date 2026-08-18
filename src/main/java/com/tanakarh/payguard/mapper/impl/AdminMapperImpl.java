package com.tanakarh.payguard.mapper.impl;

import org.springframework.stereotype.Component;

import com.tanakarh.payguard.domain.dto.request.AdminDto;
import com.tanakarh.payguard.domain.dto.response.AdminResponseDto;
import com.tanakarh.payguard.domain.entity.user.admin.Admin;
import com.tanakarh.payguard.mapper.AdminMapper;

@Component
public class AdminMapperImpl implements AdminMapper {

    @Override
    public Admin toEntity(AdminDto adminDto) {
        return Admin.builder()
                    .name(adminDto.name())
                    .email(adminDto.email())
                    .password(adminDto.password())
                    .build();
    }

    @Override
    public AdminResponseDto toResponseDto(Admin admin) {
       return new AdminResponseDto(
        admin.getId(),
        admin.getName(),
        admin.getEmail()
       );
    }
}
