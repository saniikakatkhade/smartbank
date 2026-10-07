package com.smartbook.fraud;

import java.math.BigDecimal;

public class SameAccountFraudStrategy
        implements FraudDetectionStrategy {

    @Override
    public boolean isFraud(
            String senderEmail,
            String receiverEmail,
            BigDecimal amount) {

        return senderEmail != null
                && senderEmail.equalsIgnoreCase(receiverEmail);
    }
}