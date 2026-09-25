package com.ecommerce.payment.transaction;

import com.ecommerce.payment.gateway.ChargeResult;
import com.ecommerce.payment.gateway.PaymentGateway;

import java.math.BigDecimal;

/**
 * BRIDGE
 */
public class OneTimePayment extends PaymentTransaction {

    public OneTimePayment(PaymentGateway gateway, BigDecimal amount, String currency,
                           String customerId, String customerEmail, String cardLast4) {
        super(gateway, amount, currency, customerId, customerEmail, cardLast4);
    }

    @Override
    public TransactionResult charge() {
        ChargeResult result = gateway.processCharge(buildChargeRequest("One-time purchase"));
        return new TransactionResult(result.isSuccess(), result.getTransactionId(),
                gateway.getGatewayName(), result.getMessage(), 1);
    }
}
