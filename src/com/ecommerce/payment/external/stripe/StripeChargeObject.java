package com.ecommerce.payment.external.stripe;

/** Simplified stand-in for Stripe's charge response object. */
public final class StripeChargeObject {

    private final String id;
    private final String status; // "succeeded", "failed"
    private final String failureMessage;

    public StripeChargeObject(String id, String status, String failureMessage) {
        this.id = id;
        this.status = status;
        this.failureMessage = failureMessage;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getFailureMessage() {
        return failureMessage;
    }
}
