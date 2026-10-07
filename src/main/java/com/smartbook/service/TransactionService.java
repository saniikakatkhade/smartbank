package com.smartbook.service;

import com.smartbook.entity.Transaction;
import com.smartbook.kafka.TransactionEvent;
import com.smartbook.kafka.TransactionKafkaProducer;
import com.smartbook.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionKafkaProducer transactionKafkaProducer;

    public TransactionService(
            TransactionRepository transactionRepository,
            TransactionKafkaProducer transactionKafkaProducer) {

        this.transactionRepository = transactionRepository;
        this.transactionKafkaProducer = transactionKafkaProducer;
    }

    public void recordTransaction(
            String type,
            BigDecimal amount,
            String senderEmail,
            String receiverEmail,
            String status,
            Long senderAccountId,
            Long receiverAccountId) {

        Transaction transaction = new Transaction(
                type,
                amount,
                senderEmail,
                receiverEmail,
                LocalDateTime.now(),
                status
        );

        // Save transaction to PostgreSQL
        Transaction savedTransaction =
                transactionRepository.save(transaction);

        // Create Kafka event
        TransactionEvent event = new TransactionEvent(
                savedTransaction.getId(),
                senderAccountId,
                receiverAccountId,
                amount.doubleValue(),
                type,
                status,
                savedTransaction.getTimestamp()
        );

        // Send event to Kafka
        transactionKafkaProducer.sendTransactionEvent(event);
    }

    public Page<Transaction> getUserTransactions(
            String email,
            Pageable pageable) {

        return transactionRepository
                .findBySenderEmailOrReceiverEmail(
                        email,
                        email,
                        pageable
                );
    }
}