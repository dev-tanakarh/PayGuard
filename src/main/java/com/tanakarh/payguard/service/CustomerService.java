package com.tanakarh.payguard.service;

import java.util.List;

import com.tanakarh.payguard.domain.dto.request.CustomerDto;
import com.tanakarh.payguard.domain.dto.response.CustomerResponseDto;

public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerDto customerDto);
    CustomerResponseDto getCustomerById(Long id);
    CustomerResponseDto getCustomerByEmail(String email);
    List<CustomerResponseDto> getAllCustomers();
    CustomerResponseDto updateCustomer(Long id, CustomerDto customerDto);
    void activateCustomer(Long id);
    void deactivateCustomer(Long id);
    void deleteCustomer(Long id);
}
