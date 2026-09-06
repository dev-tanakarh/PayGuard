package com.tanakarh.payguard.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tanakarh.payguard.Repository.CustomerRepository;
import com.tanakarh.payguard.Repository.MerchantRepository;
import com.tanakarh.payguard.Repository.PaymentRepository;
import com.tanakarh.payguard.Repository.TransactionRepository;
import com.tanakarh.payguard.domain.dto.request.TransactionDto;
import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;
import com.tanakarh.payguard.domain.entity.payment.Payment;
import com.tanakarh.payguard.domain.entity.transaction.Transaction;
import com.tanakarh.payguard.domain.entity.transaction.TransactionStatus;
import com.tanakarh.payguard.domain.entity.transaction.TransactionType;
import com.tanakarh.payguard.mapper.TransactionMapper;
import com.tanakarh.payguard.service.TransactionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepo;
    private final PaymentRepository paymentRepo;
    private final TransactionMapper transactionMapper;
    private final MerchantRepository merchantRepo;
    private final CustomerRepository customerRepo;

    @Override
    @Transactional
    public TransactionResponseDto createTransaction(TransactionDto transactionDto) {
        log.info("Creating transaction for payment ID: {}", transactionDto.paymentId());
        Payment payment = paymentRepo.findById(transactionDto.paymentId())
                .orElseThrow(() -> {
                    log.error("Payment not found with ID: {}", transactionDto.paymentId());
                    return new RuntimeException("Payment not found");
                });

        Transaction transaction = transactionMapper.toEntity(transactionDto);
        transaction.setTransactionReference(generateTransactionReference());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setType(TransactionType.REFUND);
        transaction.setPayment(payment);
        Transaction savedTransaction = transactionRepo.save(transaction);
        log.info("Transaction created successfully - Reference: {}", savedTransaction.getTransactionReference());
        return transactionMapper.toResponseDto(savedTransaction);
    }

    @Override
    public TransactionResponseDto getTransactionById(Long id) {
        log.debug("Fetching transaction with ID: {}", id);
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Transaction not found with ID: {}", id);
                    return new RuntimeException("Transaction not found");
                });
        log.debug("Transaction found: {}", transaction.getTransactionReference());
        return transactionMapper.toResponseDto(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDto updateTransaction(Long id, TransactionDto transactionDto) {
        log.info("Updating transaction with ID: {}", id);
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Transaction not found with ID: {}", id);
                    return new RuntimeException("Transaction not found");
                });

        transaction.setAmount(transactionDto.amount());
        transaction.setCurrency(transactionDto.currency());
        Transaction updatedTransaction = transactionRepo.save(transaction);
        log.info("Transaction updated successfully - ID: {}", id);
        return transactionMapper.toResponseDto(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        log.info("Deleting transaction with ID: {}", id);
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Transaction not found with ID: {}", id);
                    return new RuntimeException("Transaction not found");
                });
        transactionRepo.delete(transaction);
        log.info("Transaction deleted successfully - ID: {}", id);
    }

    @Override
    public List<TransactionResponseDto> getAllTransactions() {
        log.debug("Fetching all transactions");
        List<Transaction> transactions = transactionRepo.findAll();
        return transactions.stream()
                .map(transactionMapper::toResponseDto)
                .toList();
    }

    public String generateTransactionReference() {
        return "PG-TXN-" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByPaymentId(Long paymentId) {
        log.debug("Fetching transactions for payment ID: {}", paymentId);
        if(paymentRepo.existsById(paymentId)) {
            List<Transaction> transactions = transactionRepo.findByPaymentId(paymentId);
            log.debug("Found {} transactions for payment ID: {}", transactions.size(), paymentId);
            return transactions.stream()
                    .map(transactionMapper::toResponseDto)
                    .toList();
        } else {
            log.error("Payment not found with ID: {}", paymentId);
            throw new RuntimeException("Payment with id " + paymentId + " not found");
        }
    }

    @Override
    public List<TransactionResponseDto> getTransactionByMerchantId(Long merchantId) {
        log.debug("Fetching transactions for merchant ID: {}", merchantId);
        if(merchantRepo.existsById(merchantId)) {
            List<Transaction> transactions = transactionRepo.findByPaymentMerchantId(merchantId);
            log.debug("Found {} transactions for merchant ID: {}", transactions.size(), merchantId);
            return transactions.stream()
                    .map(transactionMapper::toResponseDto)
                    .toList();
        } else {
            log.error("Merchant not found with ID: {}", merchantId);
            throw new RuntimeException("Merchant with id " + merchantId + " not found");
        }
    }

    @Override
    public List<TransactionResponseDto> getTransactionByCustomerId(Long customerId) {
        log.debug("Fetching transactions for customer ID: {}", customerId);
        if(customerRepo.existsById(customerId)) {
            List<Transaction> transactions = transactionRepo.findByPaymentCustomerId(customerId);
            return transactions.stream()
                    .map(transactionMapper::toResponseDto)
                    .toList();
        } else {
            throw new RuntimeException("Customer with id " + customerId + " not found");
        }
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByStatus(String status) {
        TransactionStatus transactionStatus;
        try {
            transactionStatus = TransactionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid transaction status: " + status);
        }

        List<Transaction> transactions = transactionRepo.findByStatus(transactionStatus);
        return transactions.stream()
                .map(transactionMapper::toResponseDto)
                .toList();
    }

}
