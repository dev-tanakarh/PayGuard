package com.tanakarh.payguard.mapper.impl;

import org.springframework.stereotype.Component;

import com.tanakarh.payguard.domain.dto.request.PaymentRequestDto;
import com.tanakarh.payguard.domain.dto.response.PaymentResponseDto;
import com.tanakarh.payguard.domain.entity.payment.Payment;
import com.tanakarh.payguard.mapper.PaymentMapper;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment toEntity(PaymentRequestDto paymentRequestDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntity'");
    }

    @Override
    public PaymentResponseDto toResponseDto(Payment payment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toResponseDto'");
    }

}
