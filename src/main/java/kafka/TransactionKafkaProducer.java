package com.smartbook.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionKafkaProducer {

    private static final String TOPIC = "smartbank-transactions";

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public TransactionKafkaProducer(
            KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionEvent(TransactionEvent event) {

        try {
            kafkaTemplate.send(
                    TOPIC,
                    String.valueOf(event.getTransactionId()),
                    event
            ).get();

            System.out.println("========================================");
            System.out.println("Kafka event sent successfully");
            System.out.println("Transaction ID: " + event.getTransactionId());
            System.out.println("========================================");

        } catch (Exception e) {

            System.out.println("========================================");
            System.out.println("Kafka event failed");
            System.out.println("Transaction ID: " + event.getTransactionId());
            System.out.println("Error: " + e.getMessage());
            System.out.println("========================================");

            throw new RuntimeException("Kafka event failed", e);
        }
    }
}