package com.tanakarh.payguard.service;

import java.util.List;

import com.tanakarh.payguard.domain.dto.request.TransactionDto;
import com.tanakarh.payguard.domain.dto.response.TransactionResponseDto;

public interface TransactionService {
    TransactionResponseDto createTransaction(TransactionDto transactionDto);
    TransactionResponseDto getTransactionById(Long id);
    TransactionResponseDto updateTransaction(Long id, TransactionDto transactionDto);
    void deleteTransaction(Long id);
    List<TransactionResponseDto> getAllTransactions();
    List<TransactionResponseDto> getTransactionsByPaymentId(Long paymentId);
    //List<TransactionResponseDto> getTransactionsByStatus(String status);
    List<TransactionResponseDto> getTransactionByMerchantId(Long merchantId);
    List<TransactionResponseDto> getTransactionByCustomerId(Long customerId);

}
