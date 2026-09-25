package com.ecommerce.payment.external.mercadopago;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Stand-in for the real third-party "MercadoPago" SDK. Its API shape
 * (method names, request/response objects) is intentionally different
 * from our internal PaymentGateway contract - that mismatch is exactly
 * why MercadoPagoAdapter (ADAPTER PATTERN) is required to bridge the gap.
 */
public class MercadoPagoSDK {

    private final AtomicLong idSequence = new AtomicLong(1000);

    public MercadoPagoPaymentResponse createPayment(MercadoPagoPaymentRequest request) {
        long id = idSequence.incrementAndGet();
        // Simplified simulation: amounts below 5 units are declined, so the
        // retry logic in SubscriptionPayment has something to demonstrate.
        if (request.getTransactionAmount() < 5.0) {
            return new MercadoPagoPaymentResponse(id, "rejected", "cc_rejected_insufficient_amount");
        }
        return new MercadoPagoPaymentResponse(id, "approved", "accredited");
    }

    public MercadoPagoRefundResponse refundPayment(long paymentId, double amount) {
        return new MercadoPagoRefundResponse(idSequence.incrementAndGet(), true);
    }
}
