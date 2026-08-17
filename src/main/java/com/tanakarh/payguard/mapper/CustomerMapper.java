package com.tanakarh.payguard.mapper;

import com.tanakarh.payguard.domain.dto.request.CustomerDto;
import com.tanakarh.payguard.domain.dto.response.CustomerResponseDto;
import com.tanakarh.payguard.domain.entity.user.customer.Customer;

public interface CustomerMapper {
    Customer toEntity(CustomerDto customerDto);
    CustomerResponseDto toResponseDto(Customer customer);

}
