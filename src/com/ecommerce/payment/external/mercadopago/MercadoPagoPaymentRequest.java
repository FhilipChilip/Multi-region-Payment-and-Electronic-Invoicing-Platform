package com.ecommerce.payment.external.mercadopago;

/** Simplified stand-in for the real MercadoPago SDK's payment request payload. */
public final class MercadoPagoPaymentRequest {

    private final double transactionAmount;
    private final String currencyId;
    private final String payerEmail;
    private final String description;

    public MercadoPagoPaymentRequest(double transactionAmount, String currencyId,
                                      String payerEmail, String description) {
        this.transactionAmount = transactionAmount;
        this.currencyId = currencyId;
        this.payerEmail = payerEmail;
        this.description = description;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public String getDescription() {
        return description;
    }
}
