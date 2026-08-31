package com.tanakarh.payguard.domain.dto.response;

import java.util.List;

import com.tanakarh.payguard.domain.entity.user.UserStatus;

public record CustomerActivityDto(
    Long customerId,
    String email,
    UserStatus status,
    List<PaymentResponseDto> recentPayments,
    List<TransactionResponseDto> recentTransactions
) {

}
