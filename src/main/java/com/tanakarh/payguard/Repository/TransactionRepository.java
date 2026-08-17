package com.tanakarh.payguard.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanakarh.payguard.entity.transaction.Transaction;
import com.tanakarh.payguard.entity.transaction.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> findByPaymentCustomerId(Long customerId);
    Optional<Transaction> findByTransactionReference(String transactionReference);
    boolean existsByTransactionReference(String transactionReference);
    List<Transaction> findByPaymentId(Long paymentId);
    List<Transaction> findByPaymentMerchantId(Long merchantId);
    List<Transaction> findByType(TransactionType type);
    List<Transaction> findByStatus(com.tanakarh.payguard.entity.transaction.TransactionStatus status);
}
