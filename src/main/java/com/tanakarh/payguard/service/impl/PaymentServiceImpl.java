package com.tanakarh.payguard.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tanakarh.payguard.Repository.CustomerRepository;
import com.tanakarh.payguard.Repository.MerchantRepository;
import com.tanakarh.payguard.Repository.PaymentRepository;
import com.tanakarh.payguard.Repository.TransactionRepository;
import com.tanakarh.payguard.domain.dto.request.PaymentRequestDto;
import com.tanakarh.payguard.domain.dto.response.PaymentResponseDto;
import com.tanakarh.payguard.domain.entity.payment.Payment;
import com.tanakarh.payguard.domain.entity.payment.PaymentStatus;
import com.tanakarh.payguard.domain.entity.transaction.Transaction;
import com.tanakarh.payguard.domain.entity.transaction.TransactionStatus;
import com.tanakarh.payguard.domain.entity.transaction.TransactionType;
import com.tanakarh.payguard.domain.entity.user.UserStatus;
import com.tanakarh.payguard.domain.entity.user.customer.Customer;
import com.tanakarh.payguard.domain.entity.user.merchant.Merchant;
import com.tanakarh.payguard.exception.InvalidOperationException;
import com.tanakarh.payguard.exception.UserNotFoundException;
import com.tanakarh.payguard.mapper.PaymentMapper;
import com.tanakarh.payguard.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepo;
    private final MerchantRepository merchantRepo;
    private final TransactionRepository transactionRepo;

    @Override
    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
        log.info("Creating payment from customer {} to merchant {}, amount: {}",
             paymentRequestDto.customerId(), 
             paymentRequestDto.merchantId(),
             paymentRequestDto.amount());
    
    // Validate customer
    Customer customer = customerRepo.findById(paymentRequestDto.customerId())
        .orElseThrow(() -> new UserNotFoundException("Customer not found"));
    
    if (customer.getUser().getStatus() != UserStatus.ACTIVE) {
        log.warn("Payment blocked - customer inactive: {}", customer.getId());
        throw new InvalidOperationException("Customer account is inactive");
    }
    
    // Validate merchant
    Merchant merchant = merchantRepo.findById(paymentRequestDto.merchantId())
        .orElseThrow(() -> new UserNotFoundException("Merchant not found"));
    
    if (merchant.getUser().getStatus() != UserStatus.ACTIVE) {
        throw new InvalidOperationException("Merchant account is inactive");
    }
    
    // Validate amount
    if (paymentRequestDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidOperationException("Payment amount must be positive");
    }
    
    if (paymentRequestDto.amount().compareTo(new BigDecimal("999999.99")) > 0) {
        throw new InvalidOperationException("Payment amount exceeds maximum limit");
    }

        Payment payment = paymentMapper.toEntity(paymentRequestDto);
        payment.setCustomer(customer);
        payment.setMerchant(merchant);
        payment.setPaymentReference(generatePaymentReference());
        payment.setStatus(PaymentStatus.PENDING);
        Payment savedPayment = paymentRepository.save(payment);

        Transaction transaction = new Transaction();
        transaction.setPayment(savedPayment);
        transaction.setTransactionReference(generateTransactionReference());
        transaction.setAmount(savedPayment.getAmount());
        transaction.setCurrency(savedPayment.getCurrency());
        transaction.setType(TransactionType.PAYMENT);
        transaction.setStatus(TransactionStatus.PENDING);

        transactionRepo.save(transaction);
        
        log.info("Payment created successfully - Reference: {}, Status: {}", 
                 payment.getPaymentReference(), payment.getStatus());

        return paymentMapper.toResponseDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponseDto getPaymentById(Long id) {
        log.debug("Fetching payment with ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                                .orElseThrow(() -> {
                                    log.error("Payment not found with ID: {}", id);
                                    return new RuntimeException("Payment not found");
                                });
        log.debug("Payment found: {}", payment.getPaymentReference());
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public List<PaymentResponseDto> getAllPayments() {
        log.debug("Fetching all payments");
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                        .map(paymentMapper::toResponseDto)
                        .toList();
    }

    @Override
    @Transactional
    public void updatePayment(Long id, PaymentRequestDto paymentRequestDto) {
        log.info("Updating payment with ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                                .orElseThrow(() -> {
                                    log.error("Payment not found with ID: {}", id);
                                    return new RuntimeException("Payment not found");
                                });
        payment.setAmount(paymentRequestDto.amount());
        payment.setCurrency(paymentRequestDto.currency());
        paymentRepository.save(payment);
        log.info("Payment updated successfully - ID: {}", id);
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        log.info("Deleting payment with ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                                .orElseThrow(() -> {
                                    log.error("Payment not found with ID: {}", id);
                                    return new RuntimeException("Payment not found");
                                });
        paymentRepository.delete(payment);
        log.info("Payment deleted successfully - ID: {}", id);
    }
        
    public String generatePaymentReference() {
        return "PG-PAY-" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();
    }

    public String generateTransactionReference() {
        return "PG-TXN-" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();
    }

    @Override
    public List<PaymentResponseDto> getPaymentsByCustomerId(Long customerId) {
        log.debug("Fetching payments for customer ID: {}", customerId);
        if(customerRepo.existsById(customerId)) {
            List<Payment> payments = paymentRepository.findByCustomerId(customerId);
            log.debug("Found {} payments for customer ID: {}", payments.size(), customerId);
            return payments.stream()
                    .map(paymentMapper::toResponseDto)
                    .toList();
        } else {
            log.error("Customer not found with ID: {}", customerId);
            throw new UserNotFoundException("Customer with ID " + customerId + " not found");
        }
    }

    @Override
    public List<PaymentResponseDto> getPaymentsByMerchantId(Long merchantId) {
        log.debug("Fetching payments for merchant ID: {}", merchantId);
        if(merchantRepo.existsById(merchantId)) {
            List<Payment> payments = paymentRepository.findByMerchantId(merchantId);
            log.debug("Found {} payments for merchant ID: {}", payments.size(), merchantId);
            return payments.stream()
                    .map(paymentMapper::toResponseDto)
                    .toList();
        } else {
            log.error("Merchant not found with ID: {}", merchantId);
            throw new UserNotFoundException("Merchant with ID " + merchantId + " not found");
        }
    }

    @Override
    public List<PaymentResponseDto> getPaymentByStatus(PaymentStatus status) {
        PaymentStatus paymentStatus;
        try {
            paymentStatus = status;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + status);
        }

        List<Payment> payments = paymentRepository.findByStatus(paymentStatus);
        return payments.stream()
                .map(paymentMapper::toResponseDto)
                .toList();
    }

}
