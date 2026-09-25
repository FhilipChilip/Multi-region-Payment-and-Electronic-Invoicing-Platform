package com.ecommerce.payment.adapter;

import com.ecommerce.payment.external.mercadopago.MercadoPagoPaymentRequest;
import com.ecommerce.payment.external.mercadopago.MercadoPagoPaymentResponse;
import com.ecommerce.payment.external.mercadopago.MercadoPagoRefundResponse;
import com.ecommerce.payment.external.mercadopago.MercadoPagoSDK;
import com.ecommerce.payment.gateway.ChargeRequest;
import com.ecommerce.payment.gateway.ChargeResult;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.ecommerce.payment.gateway.RefundResult;

import java.math.BigDecimal;

/**
 * ADAPTER PATTERN.
 *
 * Translates the standard PaymentGateway contract into calls against the
 * third-party MercadoPagoSDK, whose request/response shapes differ from
 * our internal model (primitive double amounts, numeric ids, and
 * "approved" / "rejected" status strings instead of a boolean).
 */
public class MercadoPagoAdapter implements PaymentGateway {

    private final MercadoPagoSDK mercadoPagoSDK;

    public MercadoPagoAdapter() {
        this(new MercadoPagoSDK());
    }

    public MercadoPagoAdapter(MercadoPagoSDK mercadoPagoSDK) {
        this.mercadoPagoSDK = mercadoPagoSDK;
    }

    @Override
    public ChargeResult processCharge(ChargeRequest request) {
        MercadoPagoPaymentRequest mpRequest = new MercadoPagoPaymentRequest(
                request.getAmount().doubleValue(),
                request.getCurrency(),
                request.getCustomerEmail(),
                request.getDescription());

        MercadoPagoPaymentResponse mpResponse = mercadoPagoSDK.createPayment(mpRequest);
        boolean success = "approved".equalsIgnoreCase(mpResponse.getStatus());

        return new ChargeResult(
                success,
                String.valueOf(mpResponse.getId()),
                "MercadoPago status: " + mpResponse.getStatus() + " (" + mpResponse.getStatusDetail() + ")");
    }

    @Override
    public RefundResult refund(String originalTransactionId, BigDecimal amount, String currency) {
        MercadoPagoRefundResponse response =
                mercadoPagoSDK.refundPayment(Long.parseLong(originalTransactionId), amount.doubleValue());
        return new RefundResult(
                response.isApproved(),
                String.valueOf(response.getRefundId()),
                response.isApproved() ? "Refund accredited" : "Refund rejected");
    }

    @Override
    public String getGatewayName() {
        return "MercadoPago";
    }
}
