package com.ecommerce.payment.fraud;

import com.ecommerce.payment.gateway.ChargeRequest;

import java.math.BigDecimal;

/** Simulates PSD2-style Strong Customer Authentication (SCA) rules used across the EU. */
public class EuFraudValidator implements FraudValidator {

    private static final BigDecimal SCA_THRESHOLD = new BigDecimal("30");

    @Override
    public FraudCheckResult validate(ChargeRequest request) {
        if (request.getAmount().compareTo(SCA_THRESHOLD) > 0) {
            // In a real system this would trigger a Strong Customer Authentication
            // challenge (3-D Secure) instead of passing straight through.
            return new FraudCheckResult(true, 40,
                    "Passed, but PSD2 Strong Customer Authentication would be required");
        }
        return new FraudCheckResult(true, 5, "Passed EU standard risk checks (below SCA threshold)");
    }
}
