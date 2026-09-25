package com.ecommerce.payment.external.stripe;

/** Simplified stand-in for Stripe's refund response object. */
public final class StripeRefundObject {

    private final String id;
    private final String status; // "succeeded", "failed"

    public StripeRefundObject(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
