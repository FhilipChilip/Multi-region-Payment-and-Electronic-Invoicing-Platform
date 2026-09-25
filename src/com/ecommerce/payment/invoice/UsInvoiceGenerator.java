package com.ecommerce.payment.invoice;

import com.ecommerce.payment.model.Customer;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Applies a simplified flat US sales-tax rate (a real system would look up state/local rates). */
public class UsInvoiceGenerator implements InvoiceGenerator {

    private static final BigDecimal FLAT_SALES_TAX_RATE = new BigDecimal("0.07");

    @Override
    public Invoice generateInvoice(String transactionId, Customer customer, BigDecimal amount, String currency) {
        BigDecimal tax = amount.multiply(FLAT_SALES_TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = amount.add(tax);

        return new Invoice("US-INV-" + transactionId, customer.getName(), amount, tax, total,
                currency, "Sales Tax 7% (simplified flat rate)");
    }
}
