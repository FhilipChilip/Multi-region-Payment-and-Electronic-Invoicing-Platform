package com.ecommerce.payment.fraud;

import com.ecommerce.payment.gateway.ChargeRequest;

import java.math.BigDecimal;

/** Simulates LATAM-specific risk rules (stricter checks on high-value first payments). */
public class LatamFraudValidator implements FraudValidator {

    private static final BigDecimal HIGH_RISK_THRESHOLD = new BigDecimal("2000");

    @Override
    public FraudCheckResult validate(ChargeRequest request) {
        if (request.getAmount().compareTo(HIGH_RISK_THRESHOLD) > 0) {
            return new FraudCheckResult(false, 85,
                    "Amount exceeds the LATAM high-risk threshold; manual review required");
        }
        return new FraudCheckResult(true, 10, "Passed LATAM standard risk checks");
    }
}
