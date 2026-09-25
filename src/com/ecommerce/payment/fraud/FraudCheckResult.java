package com.ecommerce.payment.fraud;

/** Outcome of a fraud/risk check performed before a charge is attempted. */
public final class FraudCheckResult {

    private final boolean passed;
    private final int riskScore; // 0 (safe) - 100 (very risky)
    private final String reason;

    public FraudCheckResult(boolean passed, int riskScore, String reason) {
        this.passed = passed;
        this.riskScore = riskScore;
        this.reason = reason;
    }

    public boolean isPassed() {
        return passed;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public String getReason() {
        return reason;
    }
}
