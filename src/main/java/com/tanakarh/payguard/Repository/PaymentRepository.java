package com.tanakarh.payguard.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanakarh.payguard.domain.entity.payment.Payment;
import com.tanakarh.payguard.domain.entity.payment.PaymentStatus;



@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    Optional<Payment> findByPaymentReference(String paymentReference);
    List<Payment> findByCustomerId(Long customerId);
    boolean existsByPaymentReference(String paymentReference);
    List<Payment> findByMerchantId(Long merchantId);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findTop10ByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Payment> findTop10ByOrderByCreatedAtDesc();
}
