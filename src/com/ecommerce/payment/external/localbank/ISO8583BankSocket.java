package com.ecommerce.payment.external.localbank;

/**
 * Stand-in for a local bank's authorization socket speaking ISO 8583, the
 * messaging standard used by card networks and many local banks for
 * financial transaction messages. Real integrations exchange raw,
 * fixed-format byte messages over a persistent socket; here that is
 * simulated with a simplified pipe-delimited text representation so the
 * sample stays runnable without real network sockets, while still forcing
 * LocalBankISOAdapter to do real protocol-translation work.
 *
 * Simplified request layout:  "MTI|DE2(pseudo-PAN)|DE4(amount, minor units)|DE41(terminalId)"
 * Simplified response layout: "MTI|DE39(responseCode)|DE37(retrievalReferenceNumber)"
 * Response code "00" means approved; any other code means declined.
 */
public class ISO8583BankSocket {

    private long referenceSequence = 100000L;

    public String sendIsoMessage(String isoRequestMessage) {
        String[] fields = isoRequestMessage.split("\\|");
        long amountMinorUnits = Long.parseLong(fields[2]);
        String responseCode = amountMinorUnits < 500 ? "05" : "00"; // "05" = do not honor
        String retrievalReferenceNumber = String.valueOf(++referenceSequence);
        String responseMti = "0210"; // financial transaction response
        return responseMti + "|" + responseCode + "|" + retrievalReferenceNumber;
    }
}
