package com.tanakarh.payguard.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tanakarh.payguard.Repository.CustomerRepository;
import com.tanakarh.payguard.Repository.UserRepository;
import com.tanakarh.payguard.domain.dto.request.CustomerDto;
import com.tanakarh.payguard.domain.dto.response.CustomerResponseDto;
import com.tanakarh.payguard.domain.entity.user.Role;
import com.tanakarh.payguard.domain.entity.user.User;
import com.tanakarh.payguard.domain.entity.user.UserStatus;
import com.tanakarh.payguard.domain.entity.user.customer.Customer;
import com.tanakarh.payguard.exception.UserAlreadyExistsException;
import com.tanakarh.payguard.exception.UserNotFoundException;
import com.tanakarh.payguard.mapper.CustomerMapper;
import com.tanakarh.payguard.service.CustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerDto customerDto) {
        if (userRepo.existsByEmail(customerDto.email())) {
            throw new UserAlreadyExistsException("A customer with this email already exists");
    
        }
        User user = new User();
        user.setEmail(customerDto.email());
        user.setPasswordHash(passwordEncoder.encode(customerDto.password()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.CUSTOMER);
        userRepo.save(user);

        Customer customer = customerMapper.toEntity(customerDto);
        customer.setUser(user);
        Customer savedCustomer = customerRepo.save(customer);
        return customerMapper.toResponseDto(savedCustomer);
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepo
                                .findById(id)
                                .orElseThrow(() -> 
                                       new UserNotFoundException("Customer not found")
                                    );

        return customerMapper.toResponseDto(customer);
    }

    @Override
    public CustomerResponseDto getCustomerByEmail(String email) {
        Customer customer = customerRepo
                            .findByEmail(email)
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
    public CustomerResponseDto updateCustomer(Long id, CustomerDto customerDto) {
        if (!customerRepo.existsById(id)) {
            throw new UserNotFoundException("Customer not found");
        }
        Customer customer = customerMapper.toEntity(customerDto);
        Customer updatedCustomer = customerRepo.save(customer);
        return customerMapper.toResponseDto(updatedCustomer);
    }

    @Override
    @Transactional
    public void activateCustomer(Long id) {
        Customer customer = customerRepo.findById(id)
                                .orElseThrow(() ->
                                    new UserNotFoundException("Customer not found")
                                );
        customer.getUser().setStatus(UserStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void deactivateCustomer(Long id) {
        Customer customer = customerRepo.findById(id)
                                .orElseThrow(() ->
                                    new UserNotFoundException("Customer not found")
                                );
        customer.getUser().setStatus(UserStatus.DEACTIVATED);
    }

}
