package com.tanakarh.payguard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanakarh.payguard.domain.dto.request.MerchantDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;
import com.tanakarh.payguard.service.MerchantService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping
    public MerchantResponseDto createMerchant(@Valid @RequestBody MerchantDto merchantDto) {
        return merchantService.createMerchant(merchantDto);
    }

    @GetMapping("/{id}")
    public MerchantResponseDto getMerchantBy(@PathVariable Long id) {
        return merchantService.getMerchantById(id);
    }

    @PatchMapping("/{id}")
    public MerchantResponseDto updateMerchant(@PathVariable Long id, @Valid @RequestBody MerchantDto merchantDto){
        return merchantService.updateMerchant(id, merchantDto);
    }
}

