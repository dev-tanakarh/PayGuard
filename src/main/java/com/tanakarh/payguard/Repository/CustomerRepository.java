package com.tanakarh.payguard.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanakarh.payguard.entity.user.customer.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
}
