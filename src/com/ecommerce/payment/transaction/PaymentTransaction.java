package com.ecommerce.payment.transaction;

import com.ecommerce.payment.gateway.ChargeRequest;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.ecommerce.payment.gateway.RefundResult;

import java.math.BigDecimal;

/**
 * BRIDGE PATTERN - Abstraction.
 */
public abstract class PaymentTransaction {

    protected final PaymentGateway gateway;
    protected final BigDecimal amount;
    protected final String currency;
    protected final String customerId;
    protected final String customerEmail;
    protected final String cardLast4;

    protected PaymentTransaction(PaymentGateway gateway, BigDecimal amount, String currency,
                                  String customerId, String customerEmail, String cardLast4) {
        this.gateway = gateway;
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
        this.customerEmail = customerEmail;
        this.cardLast4 = cardLast4;
    }

    /** Executes the charge, applying whatever policy this refinement defines. */
    public abstract TransactionResult charge();

    /** Refunds are delegated straight to the gateway; no refinement-specific policy is needed. */
    public TransactionResult refund(String originalTransactionId) {
        RefundResult result = gateway.refund(originalTransactionId, amount, currency);
        return new TransactionResult(result.isSuccess(), result.getRefundId(),
                gateway.getGatewayName(), result.getMessage(), 1);
    }

    protected ChargeRequest buildChargeRequest(String description) {
        return new ChargeRequest(amount, currency, customerId, customerEmail, cardLast4, description);
    }

    public PaymentGateway getGateway() {
        return gateway;
    }
}
