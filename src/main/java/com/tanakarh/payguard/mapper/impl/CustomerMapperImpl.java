package com.tanakarh.payguard.mapper.impl;

import org.springframework.stereotype.Component;

import com.tanakarh.payguard.domain.dto.request.CustomerDto;
import com.tanakarh.payguard.domain.dto.response.CustomerResponseDto;
import com.tanakarh.payguard.domain.entity.user.customer.Customer;
import com.tanakarh.payguard.mapper.CustomerMapper;

@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerDto customerDto) {
        return Customer.builder()
                       .firstName(customerDto.firstName())
                       .lastName(customerDto.lastName())
                       .build();
    }

    @Override
    public CustomerResponseDto toResponseDto(Customer customer) {
        return new CustomerResponseDto(
            customer.getId(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getUser().getStatus(),
            customer.getUser().getEmail(),
            customer.getCreatedAt(),
            customer.getUpdatedAt()
        );
    }

}
