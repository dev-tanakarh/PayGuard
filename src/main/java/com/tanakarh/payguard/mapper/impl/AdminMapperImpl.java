package com.tanakarh.payguard.mapper.impl;

import org.springframework.stereotype.Component;

import com.tanakarh.payguard.domain.dto.request.AdminDto;
import com.tanakarh.payguard.domain.dto.response.AdminResponseDto;
import com.tanakarh.payguard.domain.entity.user.Admin;
import com.tanakarh.payguard.mapper.AdminMapper;

@Component
public class AdminMapperImpl implements AdminMapper {

    @Override
    public Admin toEntity(AdminDto adminDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntity'");
    }

    @Override
    public AdminResponseDto toResponseDto(Admin admin) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toResponseDto'");
    }

}
