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

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepo;
    private final PaymentRepository paymentRepo;
    private final TransactionMapper transactionMapper;
    private final MerchantRepository merchantRepo;
    private final CustomerRepository customerRepo;

    @Override
    @Transactional
    public TransactionResponseDto createTransaction(TransactionDto transactionDto) {
        Payment payment = paymentRepo.findById(transactionDto.paymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Transaction transaction = transactionMapper.toEntity(transactionDto);
        transaction.setTransactionReference(generateTransactionReference());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setType(TransactionType.REFUND);
        transaction.setPayment(payment);
        transactionRepo.save(transaction);
        return transactionMapper.toResponseDto(transaction);
    }

    @Override
    public TransactionResponseDto getTransactionById(Long id) {
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return transactionMapper.toResponseDto(transaction);
    }

    @Override
    public TransactionResponseDto updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(transactionDto.amount());
        transaction.setCurrency(transactionDto.currency());
        transactionRepo.save(transaction);
        return transactionMapper.toResponseDto(transaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transactionRepo.delete(transaction);
    }

    @Override
    public List<TransactionResponseDto> getAllTransactions() {
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
        if(paymentRepo.existsById(paymentId)) {
            List<Transaction> transactions = transactionRepo.findByPaymentId(paymentId);
            return transactions.stream()
                    .map(transactionMapper::toResponseDto)
                    .toList();
        } else {
            throw new RuntimeException("Payment with id " + paymentId + " not found");
        }
    }

    @Override
    public List<TransactionResponseDto> getTransactionByMerchantId(Long merchantId) {
        if(merchantRepo.existsById(merchantId)) {
            List<Transaction> transactions = transactionRepo.findByPaymentMerchantId(merchantId);
            return transactions.stream()
                    .map(transactionMapper::toResponseDto)
                    .toList();
        } else {
            throw new RuntimeException("Merchant with id " + merchantId + " not found");
        }
    }

    @Override
    public List<TransactionResponseDto> getTransactionByCustomerId(Long customerId) {
        if(customerRepo.existsById(customerId)) {
            List<Transaction> transactions = transactionRepo.findByPaymentCustomerId(customerId);
            return transactions.stream()
                    .map(transactionMapper::toResponseDto)
                    .toList();
        } else {
            throw new RuntimeException("Customer with id " + customerId + " not found");
        }
    }

}
