package com.tanakarh.payguard.service;

import java.util.List;

import com.tanakarh.payguard.domain.dto.request.AdminDto;
import com.tanakarh.payguard.domain.dto.response.AdminResponseDto;
import com.tanakarh.payguard.domain.dto.response.CustomerActivityDto;
import com.tanakarh.payguard.domain.dto.response.CustomerResponseDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;
import com.tanakarh.payguard.domain.dto.response.PaymentResponseDto;
import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;
import com.tanakarh.payguard.domain.entity.payment.PaymentStatus;
import com.tanakarh.payguard.domain.entity.transaction.TransactionStatus;
import com.tanakarh.payguard.domain.entity.user.UserStatus;

public interface AdminService {
    AdminResponseDto createAdmin(AdminDto adminDto);
    void deleteAdmin(Long adminId);

    // User Management
    void changeUserStatus(Long userId, UserStatus status);

    // Customer oversight
    List<CustomerResponseDto> getAllCustomers();
    CustomerActivityDto getCustomerActivity(Long customerId);

    // Merchant oversight
    MerchantResponseDto getMerchantById(Long merchantId);
    List<MerchantResponseDto> getAllMerchants();

    // Payment oversight
    List<PaymentResponseDto> getRecentPayments();
    List<PaymentResponseDto> getPaymentsByStatus(PaymentStatus paymentStatus);

    // Transaction Oversight
    List<TransactionResponseDto> getRecentTransactions();
    List<TransactionResponseDto> getTransactionByStatus(TransactionStatus transactionStatus);

}
