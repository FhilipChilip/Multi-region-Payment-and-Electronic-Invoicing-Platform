package com.ecommerce.payment.adapter;

import com.ecommerce.payment.external.stripe.StripeChargeObject;
import com.ecommerce.payment.external.stripe.StripeChargeParams;
import com.ecommerce.payment.external.stripe.StripeClient;
import com.ecommerce.payment.external.stripe.StripeRefundObject;
import com.ecommerce.payment.gateway.ChargeRequest;
import com.ecommerce.payment.gateway.ChargeResult;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.ecommerce.payment.gateway.RefundResult;

import java.math.BigDecimal;

/**
 * ADAPTER PATTERN.
 *
 * Translates the standard PaymentGateway contract into calls against the
 * third-party StripeClient, converting decimal currency amounts into the
 * integer "amount in cents" format Stripe expects and mapping its
 * succeeded/failed status strings back to a boolean result.
 */
public class StripeAdapter implements PaymentGateway {

    private final StripeClient stripeClient;

    public StripeAdapter() {
        this(new StripeClient());
    }

    public StripeAdapter(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @Override
    public ChargeResult processCharge(ChargeRequest request) {
        long amountInCents = request.getAmount().multiply(BigDecimal.valueOf(100)).longValueExact();

        StripeChargeParams params = new StripeChargeParams(
                amountInCents, request.getCurrency(), request.getCustomerEmail(), request.getDescription());

        StripeChargeObject charge = stripeClient.createCharge(params);
        boolean success = "succeeded".equalsIgnoreCase(charge.getStatus());
        String message = success ? "Stripe charge succeeded" : "Stripe charge failed: " + charge.getFailureMessage();

        return new ChargeResult(success, charge.getId(), message);
    }

    @Override
    public RefundResult refund(String originalTransactionId, BigDecimal amount, String currency) {
        long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValueExact();
        StripeRefundObject refund = stripeClient.createRefund(originalTransactionId, amountInCents);
        boolean success = "succeeded".equalsIgnoreCase(refund.getStatus());
        return new RefundResult(success, refund.getId(), success ? "Refund succeeded" : "Refund failed");
    }

    @Override
    public String getGatewayName() {
        return "Stripe";
    }
}
