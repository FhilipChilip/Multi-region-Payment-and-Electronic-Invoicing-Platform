package com.ecommerce.payment.external.stripe;

/** Simplified stand-in for Stripe's charge-creation parameters. */
public final class StripeChargeParams {

    private final long amountInCents;
    private final String currency;
    private final String receiptEmail;
    private final String description;

    public StripeChargeParams(long amountInCents, String currency, String receiptEmail, String description) {
        this.amountInCents = amountInCents;
        this.currency = currency;
        this.receiptEmail = receiptEmail;
        this.description = description;
    }

    public long getAmountInCents() {
        return amountInCents;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    public String getDescription() {
        return description;
    }
}
