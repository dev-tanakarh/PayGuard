package com.tanakarh.payguard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanakarh.payguard.domain.dto.request.MerchantDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;
import com.tanakarh.payguard.domain.dto.response.PaymentResponseDto;
import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;
import com.tanakarh.payguard.service.MerchantService;
import com.tanakarh.payguard.service.PaymentService;
import com.tanakarh.payguard.service.TransactionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/merchant")
@RequiredArgsConstructor

public class MerchantController {

    private final MerchantService merchantService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;

    @PostMapping
    public MerchantResponseDto createMerchant(@RequestBody MerchantDto merchantDto) {
        return merchantService.createMerchant(merchantDto);
    }

    @GetMapping("/{id}")
    public MerchantResponseDto getMerchantBy(@PathVariable Long id) {
        return merchantService.getMerchantById(id);
    }

    @GetMapping
    public MerchantResponseDto getMerchantByEmail(@RequestParam String email) {
        return merchantService.getMerchantByEmail(email);
    }

    @GetMapping("/all")
    public List<MerchantResponseDto> getAllMerchants() {
        return merchantService.getAllMerchants();
    }
    
    @GetMapping("/{id}/payments")
    public List<PaymentResponseDto> getPaymentsByMerchantId(@PathVariable Long id) {
        return paymentService.getPaymentsByMerchantId(id);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponseDto> getTransactionsByMerchantId(@PathVariable Long id){
        return transactionService.getTransactionByMerchantId(id);
    }

    @PatchMapping("/{id}")
    public MerchantResponseDto updateMerchant(@PathVariable Long id, @RequestBody MerchantDto merchantDto){
        return merchantService.updateMerchant(id, merchantDto);
    }
    
    @DeleteMapping("/{id}")
    public void deleteMerchant(@PathVariable Long id){
        merchantService.deleteMerchant(id);
    }
    
    
}
