package com.tanakarh.payguard.mapper;

import com.tanakarh.payguard.domain.dto.request.AdminDto;
import com.tanakarh.payguard.domain.dto.response.AdminResponseDto;
import com.tanakarh.payguard.domain.entity.user.admin.Admin;

public interface AdminMapper {
    Admin toEntity(AdminDto adminDto);
    AdminResponseDto toResponseDto(Admin admin);
}
