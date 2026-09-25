package com.ecommerce.payment.invoice;

import com.ecommerce.payment.model.Customer;
import com.ecommerce.payment.region.Country;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Applies a simplified country-level VAT ("IVA") rate for LATAM invoices. */
public class LatamInvoiceGenerator implements InvoiceGenerator {

    @Override
    public Invoice generateInvoice(String transactionId, Customer customer, BigDecimal amount, String currency) {
        BigDecimal rate = ivaRateFor(customer.getCountry());
        BigDecimal tax = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = amount.add(tax);
        String taxLabel = "IVA " + rate.multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString() + "%";

        return new Invoice("LATAM-INV-" + transactionId, customer.getName(), amount, tax, total, currency, taxLabel);
    }

    private BigDecimal ivaRateFor(Country country) {
        switch (country) {
            case COLOMBIA:
                return new BigDecimal("0.19");
            case MEXICO:
                return new BigDecimal("0.16");
            case BRAZIL:
                return new BigDecimal("0.17");
            case ARGENTINA:
                return new BigDecimal("0.21");
            default:
                return new BigDecimal("0.15");
        }
    }
}
