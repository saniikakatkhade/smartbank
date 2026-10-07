package com.smartbook.fraud;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FraudDetectionService {

    private static final BigDecimal FRAUD_LIMIT =
            new BigDecimal("100000");

    public FraudResult checkTransaction(
            String senderEmail,
            String receiverEmail,
            BigDecimal amount) {

        // Rule 1: Same account
        if (senderEmail != null
                && senderEmail.equalsIgnoreCase(receiverEmail)) {

            return new FraudResult(
                    true,
                    100,
                    "CRITICAL",
                    "Sender and receiver accounts are the same"
            );
        }

        // Rule 2: Large transaction
        if (amount.compareTo(FRAUD_LIMIT) > 0) {

            return new FraudResult(
                    true,
                    90,
                    "HIGH",
                    "Transaction amount exceeds the fraud limit"
            );
        }

        // Normal transaction
        return new FraudResult(
                false,
                10,
                "LOW",
                "Transaction appears normal"
        );
    }
}