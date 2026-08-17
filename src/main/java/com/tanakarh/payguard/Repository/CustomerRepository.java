package com.tanakarh.payguard.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tanakarh.payguard.domain.entity.user.customer.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
}
