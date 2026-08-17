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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntity'");
    }

    @Override
    public CustomerResponseDto toResponseDto(Customer customer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toResponseDto'");
    }

}
