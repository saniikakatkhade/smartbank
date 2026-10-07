package com.smartbook.repository;

import com.smartbook.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    Page<Transaction> findBySenderEmailOrReceiverEmail(
            String senderEmail,
            String receiverEmail,
            Pageable pageable
    );
}