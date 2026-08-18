package com.tanakarh.payguard.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tanakarh.payguard.domain.entity.user.merchant.Merchant;
import com.tanakarh.payguard.domain.entity.user.merchant.MerchantStatus;


public interface MerchantRepository extends JpaRepository<Merchant, Long>{
    Optional<Merchant> findByBusinessEmail(String businessEmail);
    boolean existsByBusinessEmail(String businessEmail);
    Optional<Merchant> findByRegistrationNumber(String registrationNumber);
    List<Merchant> findByStatus(MerchantStatus status);
}
