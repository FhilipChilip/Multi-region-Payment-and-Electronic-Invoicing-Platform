package com.ecommerce.payment.external.mercadopago;

/** Simplified stand-in for the real MercadoPago SDK's refund response object. */
public final class MercadoPagoRefundResponse {

    private final long refundId;
    private final boolean approved;

    public MercadoPagoRefundResponse(long refundId, boolean approved) {
        this.refundId = refundId;
        this.approved = approved;
    }

    public long getRefundId() {
        return refundId;
    }

    public boolean isApproved() {
        return approved;
    }
}
