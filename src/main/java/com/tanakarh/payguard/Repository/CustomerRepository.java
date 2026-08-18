package com.tanakarh.payguard.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.tanakarh.payguard.domain.entity.user.customer.Customer;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Optional<Customer> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
}
