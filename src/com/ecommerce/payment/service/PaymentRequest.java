package com.ecommerce.payment.service;

import java.math.BigDecimal;

/** Plain input DTO for PaymentOrchestrator, decoupled from the HTTP layer. */
public final class PaymentRequest {

    private final String countryIsoCode;
    private final String paymentType; // "ONE_TIME" or "SUBSCRIPTION"
    private final BigDecimal amount;
    private final String currency;
    private final String customerId;
    private final String customerName;
    private final String customerEmail;
    private final String customerTaxId;
    private final String cardLast4;
    private final String subscriptionId;

    public PaymentRequest(String countryIsoCode, String paymentType, BigDecimal amount, String currency,
                           String customerId, String customerName, String customerEmail, String customerTaxId,
                           String cardLast4, String subscriptionId) {
        this.countryIsoCode = countryIsoCode;
        this.paymentType = paymentType;
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerTaxId = customerTaxId;
        this.cardLast4 = cardLast4;
        this.subscriptionId = subscriptionId;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public String getPaymentType() {
        return paymentType;
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

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerTaxId() {
        return customerTaxId;
    }

    public String getCardLast4() {
        return cardLast4;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }
}
