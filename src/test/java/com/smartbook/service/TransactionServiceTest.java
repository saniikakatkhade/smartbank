package com.smartbook.service;

import com.smartbook.entity.Transaction;
import com.smartbook.kafka.TransactionKafkaProducer;
import com.smartbook.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionKafkaProducer transactionKafkaProducer;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getUserTransactionsShouldReturnTransactions() {

        Transaction transaction = new Transaction(
                "DEPOSIT",
                new BigDecimal("500.00"),
                "test123@gmail.com",
                null,
                LocalDateTime.now(),
                "SUCCESS"
        );

        when(transactionRepository.findBySenderEmailOrReceiverEmail(
                eq("test123@gmail.com"),
                eq("test123@gmail.com"),
                any()
        )).thenReturn(
                new org.springframework.data.domain.PageImpl<>(
                        List.of(transaction)
                )
        );

        org.springframework.data.domain.Page<Transaction> result =
                transactionService.getUserTransactions(
                        "test123@gmail.com",
                        org.springframework.data.domain.PageRequest.of(0, 10)
                );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(transactionRepository).findBySenderEmailOrReceiverEmail(
                eq("test123@gmail.com"),
                eq("test123@gmail.com"),
                any()
        );
    }

    @Test
    void recordTransactionShouldSaveTransaction() {

        Transaction transaction = new Transaction(
                "DEPOSIT",
                new BigDecimal("500.00"),
                "test123@gmail.com",
                null,
                LocalDateTime.now(),
                "SUCCESS"
        );

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        transactionService.recordTransaction(
                "DEPOSIT",
                new BigDecimal("500.00"),
                "test123@gmail.com",
                null,
                "SUCCESS",
                1L,
                null
        );

        verify(transactionRepository)
                .save(any(Transaction.class));

        verify(transactionKafkaProducer)
                .sendTransactionEvent(any());
    }
}