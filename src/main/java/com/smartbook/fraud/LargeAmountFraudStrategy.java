package com.smartbook.fraud;

import java.math.BigDecimal;

public class LargeAmountFraudStrategy
        implements FraudDetectionStrategy {

    private static final BigDecimal FRAUD_LIMIT =
            new BigDecimal("100000");

    @Override
    public boolean isFraud(
            String senderEmail,
            String receiverEmail,
            BigDecimal amount) {

        return amount.compareTo(FRAUD_LIMIT) > 0;
    }
}