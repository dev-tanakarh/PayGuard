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
        Payment payment = new Payment();
        payment.setAmount(paymentRequestDto.amount());
        payment.setCurrency(paymentRequestDto.currency());

        return payment;
    }

    @Override
    public PaymentResponseDto toResponseDto(Payment payment) {
        return new PaymentResponseDto(
            payment.getId(),
            payment.getPaymentReference(),
            payment.getCustomer().getId(),
            payment.getMerchant().getId(),
            payment.getAmount(),
            payment.getCurrency(),
            payment.getStatus(),
            payment.getCreatedAt(),
            payment.getUpdatedAt()
        );
    }

}
