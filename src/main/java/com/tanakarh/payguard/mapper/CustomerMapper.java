package com.tanakarh.payguard.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.tanakarh.payguard.domain.dto.request.CustomerDto;
import com.tanakarh.payguard.domain.dto.response.CustomerResponseDto;
import com.tanakarh.payguard.domain.entity.user.customer.Customer;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    Customer toEntity(CustomerDto customerDto);

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.status", target = "status")
    CustomerResponseDto toResponseDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCustomerFromDto(CustomerDto customerDto, @MappingTarget Customer customer);

}
