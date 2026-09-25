package com.ecommerce.payment.adapter;

import com.ecommerce.payment.external.localbank.ISO8583BankSocket;
import com.ecommerce.payment.gateway.ChargeRequest;
import com.ecommerce.payment.gateway.ChargeResult;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.ecommerce.payment.gateway.RefundResult;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ADAPTER PATTERN.
 *
 * Translates the standard PaymentGateway contract into raw ISO 8583-style
 * messages understood by a local bank's authorization socket. This is the
 * most involved adapter because, unlike the object-oriented MercadoPago
 * and Stripe SDKs, ISO 8583 exchanges plain delimited messages with no
 * notion of request/response objects at all.
 */
public class LocalBankISOAdapter implements PaymentGateway {

    private static final String MTI_FINANCIAL_REQUEST = "0200";
    private static final String MTI_REVERSAL_REQUEST = "0400";

    private final ISO8583BankSocket bankSocket;
    private final AtomicLong pseudoPanSequence = new AtomicLong(400000L);

    public LocalBankISOAdapter() {
        this(new ISO8583BankSocket());
    }

    public LocalBankISOAdapter(ISO8583BankSocket bankSocket) {
        this.bankSocket = bankSocket;
    }

    @Override
    public ChargeResult processCharge(ChargeRequest request) {
        long amountMinorUnits = request.getAmount().multiply(BigDecimal.valueOf(100)).longValueExact();
        String pseudoPan = "PAN" + pseudoPanSequence.incrementAndGet();

        String isoRequest = MTI_FINANCIAL_REQUEST + "|" + pseudoPan + "|" + amountMinorUnits + "|TERM01";
        String isoResponse = bankSocket.sendIsoMessage(isoRequest);

        String[] responseFields = isoResponse.split("\\|");
        String responseCode = responseFields[1];
        String retrievalReferenceNumber = responseFields[2];
        boolean approved = "00".equals(responseCode);

        return new ChargeResult(approved, retrievalReferenceNumber,
                "ISO 8583 response code " + responseCode + (approved ? " (approved)" : " (declined)"));
    }

    @Override
    public RefundResult refund(String originalTransactionId, BigDecimal amount, String currency) {
        long amountMinorUnits = amount.multiply(BigDecimal.valueOf(100)).longValueExact();
        String isoRequest = MTI_REVERSAL_REQUEST + "|REV" + originalTransactionId + "|" + amountMinorUnits + "|TERM01";
        String isoResponse = bankSocket.sendIsoMessage(isoRequest);

        String[] responseFields = isoResponse.split("\\|");
        boolean approved = "00".equals(responseFields[1]);

        return new RefundResult(approved, responseFields[2], approved ? "Reversal approved" : "Reversal declined");
    }

    @Override
    public String getGatewayName() {
        return "LocalBankISO8583";
    }
}
