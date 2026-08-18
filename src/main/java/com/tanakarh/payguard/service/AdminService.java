package com.tanakarh.payguard.service;

import com.tanakarh.payguard.domain.dto.request.AdminDto;
import com.tanakarh.payguard.domain.dto.response.AdminResponseDto;

public interface AdminService {
    AdminResponseDto createAdmin(AdminDto adminDto);
}
