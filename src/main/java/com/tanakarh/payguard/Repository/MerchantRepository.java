package com.tanakarh.payguard.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanakarh.payguard.entity.user.merchant.Merchant;
import com.tanakarh.payguard.entity.user.merchant.MerchantStatus;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long>{
    Optional<Merchant> findByBusinessEmail(String businessEmail);
    boolean existsByBusinessEmail(String businessEmail);
    List<Merchant> findByStatus(MerchantStatus status);
}
