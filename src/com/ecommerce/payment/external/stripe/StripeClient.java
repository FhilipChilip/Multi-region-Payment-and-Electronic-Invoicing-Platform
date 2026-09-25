package com.ecommerce.payment.external.stripe;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Stand-in for the real Stripe Java SDK. Note how Stripe expresses money
 * as an integer number of cents and returns "succeeded" / "failed" status
 * strings instead of a boolean - another realistic mismatch that
 * StripeAdapter (ADAPTER PATTERN) must translate.
 */
public class StripeClient {

    private final AtomicLong idSequence = new AtomicLong(5000);

    public StripeChargeObject createCharge(StripeChargeParams params) {
        String id = "ch_" + idSequence.incrementAndGet();
        if (params.getAmountInCents() < 500) { // less than 5.00 in the given currency
            return new StripeChargeObject(id, "failed", "Your card has insufficient funds.");
        }
        return new StripeChargeObject(id, "succeeded", null);
    }

    public StripeRefundObject createRefund(String chargeId, long amountInCents) {
        return new StripeRefundObject("re_" + idSequence.incrementAndGet(), "succeeded");
    }
}
