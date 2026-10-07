package com.smartbook.fraud;

import java.math.BigDecimal;

public interface FraudDetectionStrategy {

    boolean isFraud(
            String senderEmail,
            String receiverEmail,
            BigDecimal amount
    );
}