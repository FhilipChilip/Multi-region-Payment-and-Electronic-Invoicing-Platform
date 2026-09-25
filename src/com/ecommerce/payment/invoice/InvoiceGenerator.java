package com.ecommerce.payment.invoice;

import com.ecommerce.payment.model.Customer;

import java.math.BigDecimal;

/**
 * ABSTRACT FACTORY PRODUCT.
 * Each region has different tax and billing regulations, so every
 * RegionalPaymentFactory produces the InvoiceGenerator suited to its region.
 */
public interface InvoiceGenerator {
    Invoice generateInvoice(String transactionId, Customer customer, BigDecimal amount, String currency);
}
