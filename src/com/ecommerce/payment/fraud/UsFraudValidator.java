package com.ecommerce.payment.fraud;

import com.ecommerce.payment.gateway.ChargeRequest;

/** Simulates US-style AVS/CVV and watch-list screening. */
public class UsFraudValidator implements FraudValidator {

    @Override
    public FraudCheckResult validate(ChargeRequest request) {
        if (request.getCardLast4() == null || request.getCardLast4().length() != 4) {
            return new FraudCheckResult(false, 90, "Invalid card fingerprint supplied (AVS/CVV check failed)");
        }
        return new FraudCheckResult(true, 15, "Passed US standard AVS/CVV and watch-list screening");
    }
}
