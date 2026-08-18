package com.tanakarh.payguard.service;

import com.tanakarh.payguard.domain.dto.request.MerchantDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;

public interface MerchantService {
    MerchantResponseDto createMerchant(MerchantDto merchantDto);
    MerchantResponseDto getMerchantById(Long id);
    MerchantResponseDto getMerchantByEmail(String email);
    MerchantResponseDto getMerchantByRegistrationNumber(String registrationNumber);
    MerchantResponseDto updateMerchant(Long id, MerchantDto merchantDto);
    void deleteMerchant(Long id);
    void activateMerchant(Long id);
    void deactivateMerchant(Long id);
    void suspendMerchant(Long id);
    void rejectMerchant(Long id);

}
