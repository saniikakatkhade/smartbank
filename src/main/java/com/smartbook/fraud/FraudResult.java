package com.smartbook.fraud;

public class FraudResult {

    private final boolean fraud;
    private final int riskScore;
    private final String riskLevel;
    private final String reason;

    public FraudResult(
            boolean fraud,
            int riskScore,
            String riskLevel,
            String reason) {

        this.fraud = fraud;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.reason = reason;
    }

    public boolean isFraud() {
        return fraud;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getReason() {
        return reason;
    }
}