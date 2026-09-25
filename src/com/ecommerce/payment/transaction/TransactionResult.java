package com.ecommerce.payment.transaction;

/** Outcome of executing a PaymentTransaction, independent of the gateway used. */
public final class TransactionResult {

    private final boolean success;
    private final String transactionId;
    private final String gatewayName;
    private final String message;
    private final int attempts;

    public TransactionResult(boolean success, String transactionId, String gatewayName,
                              String message, int attempts) {
        this.success = success;
        this.transactionId = transactionId;
        this.gatewayName = gatewayName;
        this.message = message;
        this.attempts = attempts;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public String getMessage() {
        return message;
    }

    public int getAttempts() {
        return attempts;
    }
}
