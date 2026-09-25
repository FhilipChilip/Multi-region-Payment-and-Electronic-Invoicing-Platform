package com.ecommerce.payment.invoice;

import java.math.BigDecimal;

/** A region-compliant invoice produced after a successful charge. */
public final class Invoice {

    private final String invoiceId;
    private final String customerName;
    private final BigDecimal subtotal;
    private final BigDecimal taxAmount;
    private final BigDecimal total;
    private final String currency;
    private final String taxLabel;

    public Invoice(String invoiceId, String customerName, BigDecimal subtotal, BigDecimal taxAmount,
                    BigDecimal total, String currency, String taxLabel) {
        this.invoiceId = invoiceId;
        this.customerName = customerName;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.total = total;
        this.currency = currency;
        this.taxLabel = taxLabel;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTaxLabel() {
        return taxLabel;
    }
}
