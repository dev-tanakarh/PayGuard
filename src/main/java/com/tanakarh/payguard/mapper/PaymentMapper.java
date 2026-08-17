package com.tanakarh.payguard.mapper;

import com.tanakarh.payguard.domain.dto.request.PaymentRequestDto;
import com.tanakarh.payguard.domain.dto.response.PaymentResponseDto;
import com.tanakarh.payguard.domain.entity.payment.Payment;

public interface PaymentMapper {
    Payment toEntity(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto toResponseDto(Payment payment);
}
