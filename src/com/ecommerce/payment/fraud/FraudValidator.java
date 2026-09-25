package com.ecommerce.payment.fraud;

import com.ecommerce.payment.gateway.ChargeRequest;

/**
 * ABSTRACT FACTORY PRODUCT.
 * Each region enforces different fraud/compliance rules, so every
 * RegionalPaymentFactory produces the FraudValidator suited to its region.
 */
public interface FraudValidator {
    FraudCheckResult validate(ChargeRequest request);
}
