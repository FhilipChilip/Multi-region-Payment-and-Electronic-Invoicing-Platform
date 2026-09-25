package com.ecommerce.payment.external.mercadopago;

/** Simplified stand-in for the real MercadoPago SDK's payment response object. */
public final class MercadoPagoPaymentResponse {

    private final long id;
    private final String status;       // "approved", "rejected", "in_process"
    private final String statusDetail; // e.g. "accredited", "cc_rejected_insufficient_amount"

    public MercadoPagoPaymentResponse(long id, String status, String statusDetail) {
        this.id = id;
        this.status = status;
        this.statusDetail = statusDetail;
    }

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }
}
