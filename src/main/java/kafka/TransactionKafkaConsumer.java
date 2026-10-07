package com.smartbook.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionKafkaConsumer {

    @KafkaListener(
            topics = "smartbank-transactions",
            groupId = "smartbank-group"
    )
    public void consume(TransactionEvent event) {

        System.out.println();
        System.out.println("========================================");
        System.out.println("KAFKA TRANSACTION EVENT RECEIVED");
        System.out.println("========================================");

        System.out.println(
                "Transaction ID: " + event.getTransactionId()
        );

        System.out.println(
                "Sender Account: " + event.getSenderAccountId()
        );

        System.out.println(
                "Receiver Account: " + event.getReceiverAccountId()
        );

        System.out.println(
                "Amount: ₹" + event.getAmount()
        );

        System.out.println(
                "Transaction Type: " + event.getTransactionType()
        );

        System.out.println("========================================");
        System.out.println();
    }
}