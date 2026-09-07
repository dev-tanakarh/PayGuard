package com.tanakarh.payguard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanakarh.payguard.domain.dto.request.CustomerDto;
import com.tanakarh.payguard.domain.dto.response.CustomerResponseDto;
import com.tanakarh.payguard.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    
    @PostMapping
    public CustomerResponseDto createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return customerService.createCustomer(customerDto);
    }

    @GetMapping("/{id}")
    public CustomerResponseDto getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PatchMapping("/{id}")
    public CustomerResponseDto updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto) {
        return customerService.updateCustomer(id, customerDto);
    }
}
