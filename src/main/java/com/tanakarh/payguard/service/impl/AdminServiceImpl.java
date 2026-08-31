package com.tanakarh.payguard.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tanakarh.payguard.Repository.AdminRepository;
import com.tanakarh.payguard.Repository.CustomerRepository;
import com.tanakarh.payguard.Repository.MerchantRepository;
import com.tanakarh.payguard.Repository.PaymentRepository;
import com.tanakarh.payguard.Repository.TransactionRepository;
import com.tanakarh.payguard.Repository.UserRepository;
import com.tanakarh.payguard.domain.dto.request.AdminDto;
import com.tanakarh.payguard.domain.dto.response.AdminResponseDto;
import com.tanakarh.payguard.domain.dto.response.CustomerActivityDto;
import com.tanakarh.payguard.domain.dto.response.CustomerResponseDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;
import com.tanakarh.payguard.domain.dto.response.PaymentResponseDto;
import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;
import com.tanakarh.payguard.domain.entity.payment.Payment;
import com.tanakarh.payguard.domain.entity.payment.PaymentStatus;
import com.tanakarh.payguard.domain.entity.transaction.Transaction;
import com.tanakarh.payguard.domain.entity.transaction.TransactionStatus;
import com.tanakarh.payguard.domain.entity.user.Role;
import com.tanakarh.payguard.domain.entity.user.User;
import com.tanakarh.payguard.domain.entity.user.UserStatus;
import com.tanakarh.payguard.domain.entity.user.admin.Admin;
import com.tanakarh.payguard.domain.entity.user.customer.Customer;
import com.tanakarh.payguard.domain.entity.user.merchant.Merchant;
import com.tanakarh.payguard.exception.UserAlreadyExistsException;
import com.tanakarh.payguard.exception.UserNotFoundException;
import com.tanakarh.payguard.mapper.AdminMapper;
import com.tanakarh.payguard.mapper.CustomerMapper;
import com.tanakarh.payguard.mapper.MerchantMapper;
import com.tanakarh.payguard.mapper.PaymentMapper;
import com.tanakarh.payguard.mapper.TransactionMapper;
import com.tanakarh.payguard.service.AdminService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final AdminMapper adminMapper;
    private final CustomerRepository customerRepo;
    private final MerchantRepository merchantRepo;
    private final CustomerMapper customerMapper;
    private final MerchantMapper merchantMapper;
    private final PaymentRepository paymentRepo;
    private final TransactionRepository transactionRepo;
    private final TransactionMapper transactionMapper;
    private final PaymentMapper paymentMapper;

    @Override
    public AdminResponseDto createAdmin(AdminDto adminDto) {
        if(adminRepo.existsByEmail(adminDto.email())){
            throw new UserAlreadyExistsException("User already exists");
        }
        User user = new User();
        user.setEmail(adminDto.email());
        user.setPasswordHash(passwordEncoder.encode(adminDto.password()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.ADMIN);
        userRepo.save(user);

        Admin admin = adminMapper.toEntity(adminDto);
        admin.setUser(user);
        Admin savedAdmin = adminRepo.save(admin);
        return adminMapper.toResponseDto(savedAdmin);

    }

    @Override
    public void deleteAdmin(Long adminId) {
        Admin admin = adminRepo.findById(adminId)
                                .orElseThrow(() ->
                                    new UserNotFoundException("User not found")
                                );
        userRepo.delete(admin.getUser());
        adminRepo.delete(admin);
    }

    @Override
    public void changeUserStatus(Long userId, UserStatus status) {
        User user = userRepo.findById(userId)
                            .orElseThrow(() ->
                             new UserNotFoundException("User not Found")
                        );
        user.setStatus(status);
    }



    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        return customers.stream()
                        .map(customerMapper::toResponseDto)
                        .toList();
    }


    @Override
    public MerchantResponseDto getMerchantById(Long merchantId) {
        Merchant merchant = merchantRepo
                                .findById(merchantId)
                                .orElseThrow(() -> 
                                       new UserNotFoundException("Customer not found")
                                    );

        return merchantMapper.toResponseDto(merchant);
    }

    @Override
    public List<MerchantResponseDto> getAllMerchants() {
        List<Merchant> customers = merchantRepo.findAll();
        return customers.stream()
                        .map(merchantMapper::toResponseDto)
                        .toList();
    }

    


    

    @Override
    public List<PaymentResponseDto> getRecentPayments() {
        List<Payment> payments = paymentRepo.findTop10ByOrderByCreatedAtDesc();
        List<PaymentResponseDto> paymentResponseDtos = payments.stream()
                                                                .map(paymentMapper::toResponseDto)
                                                                .toList();

        return paymentResponseDtos;
    }

    @Override
    public List<PaymentResponseDto> getPaymentsByStatus(PaymentStatus paymentStatus) {
        List<Payment> payments = paymentRepo.findByStatus(paymentStatus);
        List<PaymentResponseDto> paymentResponseDtos = payments.stream()
                                                                .map(paymentMapper::toResponseDto)
                                                                .toList();
        return paymentResponseDtos;
    }


    @Override
    public List<TransactionResponseDto> getRecentTransactions() {
        List<Transaction> transactions = transactionRepo.findTop20ByOrderByCreatedAtDesc();
        List<TransactionResponseDto> transactionResponseDtos = transactions.stream()
                                                                .map(transactionMapper::toResponseDto)
                                                                .toList();

        return transactionResponseDtos;
    }

    

    @Override
    public CustomerActivityDto getCustomerActivity(Long customerId) {
        Customer customer = customerRepo.findById(customerId)
                                            .orElseThrow(() ->
                                                new UserNotFoundException("Customer with ID: " + customerId + " not found")
                                            );
        
        List<Payment> payments = paymentRepo.findTop10ByCustomerIdOrderByCreatedAtDesc(customerId);
        List<Transaction> transactions = transactionRepo.findTop20ByPaymentCustomerIdOrderByCreatedAtDesc(customerId);
        List<PaymentResponseDto> paymentResponseDtos = payments.stream()
                                                                .map(paymentMapper::toResponseDto)
                                                                .toList();

        List<TransactionResponseDto> transactionResponseDtos = transactions.stream()
                                                                .map(transactionMapper::toResponseDto)
                                                                .toList();

        return new CustomerActivityDto(customerId, 
                                        customer.getUser().getEmail(), 
                                        customer.getUser().getStatus(), 
                                        paymentResponseDtos, 
                                        transactionResponseDtos
                                    );

        
    }

    @Override
    public List<TransactionResponseDto> getTransactionByStatus(TransactionStatus transactionStatus) {
        List<Transaction> transactions = transactionRepo.findByStatus(transactionStatus);
        List<TransactionResponseDto> transactionResponseDtos = transactions.stream()
                                                                .map(transactionMapper::toResponseDto)
                                                                .toList();
        return transactionResponseDtos;
    }

}
