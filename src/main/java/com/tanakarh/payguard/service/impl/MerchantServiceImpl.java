package com.tanakarh.payguard.service.impl;

import java.util.List;

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
import com.tanakarh.payguard.exception.UserAlreadyExistsException;
import com.tanakarh.payguard.exception.UserNotFoundException;
import com.tanakarh.payguard.mapper.MerchantMapper;
import com.tanakarh.payguard.service.MerchantService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;
    private final MerchantRepository merchantRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MerchantResponseDto createMerchant(MerchantDto merchantDto) {
        if (userRepo.existsByEmail(merchantDto.businessEmail())) {
            throw new UserAlreadyExistsException("A merchant with this email already exists");
    
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
    @Transactional
    public MerchantResponseDto getMerchantById(Long id) {
        Merchant merchant = merchantRepo
                                .findById(id)
                                .orElseThrow(
                                    () -> new UserNotFoundException("Merchant not found")
                                );
        return merchantMapper.toResponseDto(merchant);
    }

    @Override
    @Transactional
    public MerchantResponseDto getMerchantByEmail(String email) {
        Merchant merchant = merchantRepo
                                .findByUserEmail(email)
                                .orElseThrow(
                                    () -> new UserNotFoundException("Merchant with email " + email + " not found")
                                );
        return merchantMapper.toResponseDto(merchant);
    }

    @Override
    @Transactional
    public MerchantResponseDto getMerchantByRegistrationNumber(String registrationNumber) {
        Merchant merchant = merchantRepo
                                .findByRegistrationNumber(registrationNumber)
                                .orElseThrow(
                                    () -> new UserNotFoundException("Merchant with registration number " + registrationNumber + " not found")
                                );
        return merchantMapper.toResponseDto(merchant);
    }

    @Override
    @Transactional
    public MerchantResponseDto updateMerchant(Long id, MerchantDto merchantDto) {
        if (!merchantRepo.existsById(id)) {
            throw new UserNotFoundException("Merchant not found");
        }
        Merchant merchant = merchantMapper.toEntity(merchantDto);
        Merchant updatedMerchant = merchantRepo.save(merchant);
        return merchantMapper.toResponseDto(updatedMerchant);
    }

    @Override
    public void deleteMerchant(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMerchant'");
    }

    @Override
    public void activateMerchant(Long id) {
        Merchant merchant = merchantRepo.findById(id)
                                .orElseThrow(() ->
                                    new UserNotFoundException("Merchant not found")
                                );
        merchant.getUser().setStatus(UserStatus.ACTIVE);
    }

    @Override
    public void deactivateMerchant(Long id) {
        Merchant merchant = merchantRepo.findById(id)
                                .orElseThrow(() ->
                                    new UserNotFoundException("Merchant not found")
                                );
        merchant.getUser().setStatus(UserStatus.DEACTIVATED);
    }

    @Override
    public void suspendMerchant(Long id) {
        Merchant merchant = merchantRepo.findById(id)
                                .orElseThrow(() ->
                                    new UserNotFoundException("Merchant not found")
                                );
        merchant.getUser().setStatus(UserStatus.SUSPENDED);
    }

    @Override
    public void rejectMerchant(Long id) {
        Merchant merchant = merchantRepo.findById(id)
                                .orElseThrow(() ->
                                    new UserNotFoundException("Merchant not found")
                                );
        merchant.getUser().setStatus(UserStatus.REJECTED);
    }

    @Override
    public List<MerchantResponseDto> getAllMerchants() {
        List<Merchant> merchants = merchantRepo.findAll();
        return merchants.stream()
                        .map(merchantMapper::toResponseDto)
                        .toList();
    }


}
