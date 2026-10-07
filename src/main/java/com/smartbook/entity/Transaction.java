package com.smartbook.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private String senderEmail;

    private String receiverEmail;

    private LocalDateTime timestamp;

    private String status;

    public Transaction() {
    }

    public Transaction(
            String type,
            BigDecimal amount,
            String senderEmail,
            String receiverEmail,
            LocalDateTime timestamp,
            String status) {

        this.type = type;
        this.amount = amount;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }
}