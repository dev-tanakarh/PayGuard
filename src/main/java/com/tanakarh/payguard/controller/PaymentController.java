package com.tanakarh.payguard.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tanakarh.payguard.domain.dto.request.PaymentRequestDto;
import com.tanakarh.payguard.domain.dto.response.PaymentResponseDto;
import com.tanakarh.payguard.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
        return paymentService.createPayment(paymentRequestDto);
    }

    @GetMapping("/{id}")
    public PaymentResponseDto getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/status")
    public List<PaymentResponseDto> getPaymentsByStatus(@RequestParam String status) {
        return paymentService.getPaymentByStatus(status);
    }

    @GetMapping("/customer/{customerId}")
    public List<PaymentResponseDto> getPaymentsByCustomerId(@PathVariable Long customerId) {
        return paymentService.getPaymentsByCustomerId(customerId);
    }

    @GetMapping("/merchant/{merchantId}")
    public List<PaymentResponseDto> getPaymentsByMerchantId(@PathVariable Long merchantId) {
        return paymentService.getPaymentsByMerchantId(merchantId);
    }

}
