package com.ecommerce.payment.gateway;

/** Provider-agnostic outcome of a charge attempt, produced by an Adapter. */
public final class ChargeResult {

    private final boolean success;
    private final String transactionId;
    private final String message;
    private final long timestampMillis;

    public ChargeResult(boolean success, String transactionId, String message) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
        this.timestampMillis = System.currentTimeMillis();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }
}
