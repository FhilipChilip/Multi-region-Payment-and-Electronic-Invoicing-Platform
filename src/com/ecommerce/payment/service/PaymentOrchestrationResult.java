package com.ecommerce.payment.service;

import com.ecommerce.payment.fraud.FraudCheckResult;
import com.ecommerce.payment.invoice.Invoice;
import com.ecommerce.payment.region.Region;
import com.ecommerce.payment.transaction.TransactionResult;

/** Aggregates every intermediate outcome of the flow; used for logging and by the web layer. */
public final class PaymentOrchestrationResult {

    private final Region region;
    private final String factoryUsed;
    private final String gatewayUsed;
    private final String transactionTypeUsed;
    private final FraudCheckResult fraudCheckResult;
    private final TransactionResult transactionResult; // null if blocked by the fraud check
    private final Invoice invoice;                      // null if the charge was not successful

    public PaymentOrchestrationResult(Region region, String factoryUsed, String gatewayUsed,
                                       String transactionTypeUsed, FraudCheckResult fraudCheckResult,
                                       TransactionResult transactionResult, Invoice invoice) {
        this.region = region;
        this.factoryUsed = factoryUsed;
        this.gatewayUsed = gatewayUsed;
        this.transactionTypeUsed = transactionTypeUsed;
        this.fraudCheckResult = fraudCheckResult;
        this.transactionResult = transactionResult;
        this.invoice = invoice;
    }

    public Region getRegion() {
        return region;
    }

    public String getFactoryUsed() {
        return factoryUsed;
    }

    public String getGatewayUsed() {
        return gatewayUsed;
    }

    public String getTransactionTypeUsed() {
        return transactionTypeUsed;
    }

    public FraudCheckResult getFraudCheckResult() {
        return fraudCheckResult;
    }

    public TransactionResult getTransactionResult() {
        return transactionResult;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public boolean isBlockedByFraudCheck() {
        return fraudCheckResult != null && !fraudCheckResult.isPassed();
    }
}
