package com.tanakarh.payguard.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tanakarh.payguard.domain.dto.request.TransactionDto;
import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;
import com.tanakarh.payguard.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public TransactionResponseDto createTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.createTransaction(transactionDto);
    }

    @GetMapping("/{id}")
    public TransactionResponseDto getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/payment/{paymentId}")
    public List<TransactionResponseDto> getTransactionsByPaymentId(@PathVariable Long paymentId) {
        return transactionService.getTransactionsByPaymentId(paymentId);
    }

    @GetMapping("/merchant/{merchantId}")
    public List<TransactionResponseDto> getTransactionByMerchantId(@PathVariable Long merchantId) {
        return transactionService.getTransactionByMerchantId(merchantId);
    }

    @GetMapping("/customer/{customerId}")
    public List<TransactionResponseDto> getTransactionByCustomerId(@PathVariable Long customerId) {
        return transactionService.getTransactionByCustomerId(customerId);
    }

    @GetMapping("/status/{status}")
    public List<TransactionResponseDto> getTransactionByStatus(@RequestParam String status) {
        return transactionService.getTransactionsByStatus(status);
    }
}
