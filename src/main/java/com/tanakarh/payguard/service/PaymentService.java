package com.tanakarh.payguard.service;

import java.util.List;

import com.tanakarh.payguard.domain.dto.request.PaymentRequestDto;
import com.tanakarh.payguard.domain.dto.response.PaymentResponseDto;
import com.tanakarh.payguard.domain.entity.payment.PaymentStatus;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto getPaymentById(Long id);
    List<PaymentResponseDto> getAllPayments();
    List<PaymentResponseDto> getPaymentsByCustomerId(Long customerId);
    List<PaymentResponseDto> getPaymentsByMerchantId(Long merchantId);
    List<PaymentResponseDto> getPaymentByStatus(PaymentStatus status);
    void updatePayment(Long id, PaymentRequestDto paymentRequestDto);
    void deletePayment(Long id);
}
