package com.tanakarh.payguard.service.impl;

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
import com.tanakarh.payguard.domain.entity.user.customer.Customer;
import com.tanakarh.payguard.domain.entity.user.merchant.Merchant;
import com.tanakarh.payguard.exception.UserNotFoundException;
import com.tanakarh.payguard.mapper.PaymentMapper;
import com.tanakarh.payguard.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
        Customer customer = customerRepo.findById(paymentRequestDto.customerId())
                                .orElseThrow(() -> new UserNotFoundException("Customer not found"));
        
        Merchant merchant = merchantRepo.findById(paymentRequestDto.merchantId())
                                .orElseThrow(() -> new UserNotFoundException("Merchant not found"));

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

        return paymentMapper.toResponseDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponseDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public List<PaymentResponseDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                        .map(paymentMapper::toResponseDto)
                        .toList();
    }

    @Override
    public void updatePayment(Long id, PaymentRequestDto paymentRequestDto) {
        Payment payment = paymentRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setAmount(paymentRequestDto.amount());
        payment.setCurrency(paymentRequestDto.currency());
        paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Payment not found"));
        paymentRepository.delete(payment);
    }
        
    public String generatePaymentReference() {
        return "PG-PAY-" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();
    }

    public String generateTransactionReference() {
        return "PG-TXN-" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();
    }

    @Override
    public List<PaymentResponseDto> getPaymentsByCustomerId(Long customerId) {
        if(customerRepo.existsById(customerId)) {
            List<Payment> payments = paymentRepository.findByCustomerId(customerId);
            return payments.stream()
                    .map(paymentMapper::toResponseDto)
                    .toList();
        } else {
            throw new UserNotFoundException("Customer with ID " + customerId + " not found");
        }
    }

    @Override
    public List<PaymentResponseDto> getPaymentsByMerchantId(Long merchantId) {
        if(merchantRepo.existsById(merchantId)) {
            List<Payment> payments = paymentRepository.findByMerchantId(merchantId);
            return payments.stream()
                    .map(paymentMapper::toResponseDto)
                    .toList();
        } else {
            throw new UserNotFoundException("Merchant with ID " + merchantId + " not found");
        }
    }

    @Override
    public List<PaymentResponseDto> getPaymentByStatus(String status) {
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + status);
        }

        List<Payment> payments = paymentRepository.findByStatus(paymentStatus);
        return payments.stream()
                .map(paymentMapper::toResponseDto)
                .toList();
    }

}
