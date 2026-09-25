package com.ecommerce.payment.gateway;

import java.math.BigDecimal;

/**
 * BRIDGE 
 **/
public interface PaymentGateway {

    ChargeResult processCharge(ChargeRequest request);

    RefundResult refund(String originalTransactionId, BigDecimal amount, String currency);

    /** Human-readable name of the underlying payment rail, used for logging and the UI. */
    String getGatewayName();
}
