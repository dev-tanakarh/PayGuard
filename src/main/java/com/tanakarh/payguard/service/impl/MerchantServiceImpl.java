package com.tanakarh.payguard.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tanakarh.payguard.Repository.MerchantRepository;
import com.tanakarh.payguard.Repository.UserRepository;
import com.tanakarh.payguard.domain.dto.request.MerchantDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;
import com.tanakarh.payguard.domain.entity.user.Role;
import com.tanakarh.payguard.domain.entity.user.User;
import com.tanakarh.payguard.domain.entity.user.UserStatus;
import com.tanakarh.payguard.domain.entity.user.merchant.Merchant;
import com.tanakarh.payguard.exception.CustomerAlreadyExistsException;
import com.tanakarh.payguard.mapper.MerchantMapper;
import com.tanakarh.payguard.service.MerchantService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;
    private final MerchantRepository merchantRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MerchantResponseDto createMerchant(MerchantDto merchantDto) {
        if (userRepo.existsByEmail(merchantDto.businessEmail())) {
            throw new CustomerAlreadyExistsException("A merchant with this email already exists");
    
        }
        User user = new User();
        user.setEmail(merchantDto.businessEmail());
        user.setPasswordHash(passwordEncoder.encode(merchantDto.password()));
        user.setStatus(UserStatus.PENDING_APPROVAL);
        user.setRole(Role.MERCHANT);
        userRepo.save(user);

        Merchant merchant = merchantMapper.toEntity(merchantDto);
        merchant.setUser(user);
        Merchant savedMerchant = merchantRepo.save(merchant);
        return merchantMapper.toResponseDto(savedMerchant);
    }

    @Override
    public MerchantResponseDto getMerchantById(Long id) {
        Merchant merchant = merchantRepo
                                .findById(id)
                                .orElseThrow(
                                    () -> new CustomerAlreadyExistsException("Merchant not found")
                                );
        return merchantMapper.toResponseDto(merchant);
    }

    @Override
    public MerchantResponseDto getMerchantByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMerchantByEmail'");
    }

    @Override
    public MerchantResponseDto getMerchantByRegistrationNumber(String registrationNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMerchantByRegistrationNumber'");
    }

    @Override
    public MerchantResponseDto updateMerchant(Long id, MerchantDto merchantDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMerchant'");
    }

    @Override
    public void deleteMerchant(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMerchant'");
    }

    @Override
    public void activateMerchant(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'activateMerchant'");
    }

    @Override
    public void deactivateMerchant(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivateMerchant'");
    }

    @Override
    public void suspendMerchant(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'suspendMerchant'");
    }

    @Override
    public void rejectMerchant(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rejectMerchant'");
    }


}
