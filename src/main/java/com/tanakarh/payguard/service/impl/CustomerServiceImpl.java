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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService{

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerDto customerDto) {
        log.info("Creating customer with email: {}", customerDto.email());
        if (userRepo.existsByEmail(customerDto.email())) {
            log.warn("Customer creation failed - email already exists: {}", customerDto.email());
            throw new UserAlreadyExistsException("A customer with this email already exists");
    
        }
        User user = new User();
        user.setEmail(customerDto.email());
        user.setPasswordHash(passwordEncoder.encode(customerDto.password()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.CUSTOMER);
        User savedUser = userRepo.save(user);
        log.debug("User saved with ID: {} and role: CUSTOMER", savedUser.getId());

        Customer customer = customerMapper.toEntity(customerDto);
        customer.setUser(savedUser);
        Customer savedCustomer = customerRepo.save(customer);
        log.info("Customer created successfully - ID: {}, Email: {}", savedCustomer.getId(), savedCustomer.getUser().getEmail());
        return customerMapper.toResponseDto(savedCustomer);
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        log.debug("Fetching customer with ID: {}", id);
        Customer customer = customerRepo
                                .findById(id)
                                .orElseThrow(() -> {
                                    log.error("Customer not found with ID: {}", id);
                                    return new UserNotFoundException("Customer not found");
                                });
        log.debug("Customer found: {}", customer.getUser().getEmail());
        return customerMapper.toResponseDto(customer);
    }

    @Override
    public CustomerResponseDto getCustomerByEmail(String email) {
        Customer customer = customerRepo
                            .findByUserEmail(email)
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
    @Transactional
    public CustomerResponseDto updateCustomer(Long id, CustomerDto customerDto) {
        log.info("Updating customer with ID: {}", id);
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new UserNotFoundException("Customer not found");
                });

        customerMapper.updateCustomerFromDto(customerDto, customer);
        Customer updatedCustomer = customerRepo.save(customer);
        log.info("Customer updated successfully - ID: {}", id);
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

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepo.findById(id)
                                .orElseThrow(() ->
                                    new UserNotFoundException("Customer not found")
                                );
        userRepo.delete(customer.getUser());
        customerRepo.delete(customer);
    }

}
