package com.ecommerce.payment.gateway;

import java.math.BigDecimal;

/**
 * Provider-agnostic description of a charge to be attempted.
 * Adapters translate this into whatever shape the wrapped third-party
 * SDK expects.
 */
public final class ChargeRequest {

    private final BigDecimal amount;
    private final String currency;
    private final String customerId;
    private final String customerEmail;
    private final String cardLast4;
    private final String description;

    public ChargeRequest(BigDecimal amount, String currency, String customerId,
                          String customerEmail, String cardLast4, String description) {
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
        this.customerEmail = customerEmail;
        this.cardLast4 = cardLast4;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCardLast4() {
        return cardLast4;
    }

    public String getDescription() {
        return description;
    }
}
