package com.tanakarh.payguard.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.tanakarh.payguard.service.AdminService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("app/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    AdminResponseDto createAdmin(@RequestBody AdminDto adminDto){
        return adminService.createAdmin(adminDto);
    }

    @PatchMapping("/{userId}")
    void changeUserStatus(@PathVariable Long userId, @RequestParam UserStatus status){
        adminService.changeUserStatus(userId, status);
    }

    @GetMapping("/customers")
    public List<CustomerResponseDto> getAllCustomers() {
        return adminService.getAllCustomers();
    }
    
    @GetMapping("/merchants")
    public List<MerchantResponseDto> getAllMerchants() {
        return adminService.getAllMerchants();
    }

    @GetMapping("/recentPayments")
    public List<PaymentResponseDto> getRecentPayments() {
        return adminService.getRecentPayments();
    }

    @GetMapping("/recentPayments")
    public List<PaymentResponseDto> getRecentPaymentsByStatus(@RequestParam PaymentStatus status) {
        return adminService.getPaymentsByStatus(status);
    }

    @GetMapping("/recentTransactions")
    public List<TransactionResponseDto> getRecentTransactions() {
        return adminService.getRecentTransactions();
    }

    @GetMapping("/recentTransactions")
    public List<TransactionResponseDto> getRecentPayments(@RequestParam TransactionStatus status) {
        return adminService.getTransactionByStatus(status);
    }
    
    @GetMapping("/customerActivity/{customerId}")
    public CustomerActivityDto  getCustomerActivity(@PathVariable Long customerId) {
        return adminService.getCustomerActivity(customerId);
    }
    
}

