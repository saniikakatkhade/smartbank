package com.smartbook.service;

import com.smartbook.fraud.FraudAlert;
import com.smartbook.repository.FraudAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class FraudAlertService {

    private final FraudAlertRepository fraudAlertRepository;

    public FraudAlertService(FraudAlertRepository fraudAlertRepository) {
        this.fraudAlertRepository = fraudAlertRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FraudAlert createAlert(
            Long transactionId,
            Long accountId,
            BigDecimal amount,
            Integer riskScore,
            String riskLevel,
            String reason) {

        FraudAlert alert = new FraudAlert();

        alert.setTransactionId(transactionId);
        alert.setAccountId(accountId);
        alert.setAmount(amount);
        alert.setRiskScore(riskScore);
        alert.setRiskLevel(riskLevel);
        alert.setReason(reason);
        alert.setCreatedAt(LocalDateTime.now());

        return fraudAlertRepository.save(alert);
    }
}