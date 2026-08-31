package com.tanakarh.payguard.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import com.tanakarh.payguard.Repository.AdminRepository;
import com.tanakarh.payguard.Repository.CustomerRepository;
import com.tanakarh.payguard.Repository.MerchantRepository;
import com.tanakarh.payguard.Repository.UserRepository;
import com.tanakarh.payguard.domain.dto.request.AdminDto;
import com.tanakarh.payguard.domain.dto.response.AdminResponseDto;
import com.tanakarh.payguard.domain.dto.response.CustomerResponseDto;
import com.tanakarh.payguard.domain.dto.response.MerchantResponseDto;
import com.tanakarh.payguard.domain.dto.response.PaymentResponseDto;
import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;
import com.tanakarh.payguard.domain.entity.payment.PaymentStatus;
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
    public CustomerResponseDto getCustomerById(Long customerId) {
        Customer customer = customerRepo
                                .findById(customerId)
                                .orElseThrow(() -> 
                                       new UserNotFoundException("Customer not found")
                                    );

        return customerMapper.toResponseDto(customer);
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
    public PaymentResponseDto getPaymentById(Long paymentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentById'");
    }

    @Override
    public List<PaymentResponseDto> getRecentPayments() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecentPayments'");
    }

    @Override
    public List<PaymentResponseDto> getPaymentsByStatus(PaymentStatus paymentStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentByStatus'");
    }


    @Override
    public List<TransactionResponseDto> getRecentTransactions() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecentTransactions'");
    }

    @Override
    public TransactionResponseDto getTransactionByStatus(TransactionStatus transactionStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransactionByStatus'");
    }

    @Override
    public void getCustomerActivity(Long customerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCustomerActivity'");
    }

}
