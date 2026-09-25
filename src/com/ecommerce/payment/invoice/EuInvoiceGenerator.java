package com.ecommerce.payment.invoice;

import com.ecommerce.payment.model.Customer;
import com.ecommerce.payment.region.Country;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Applies EU VAT rules, including a simplified reverse-charge note for B2B customers. */
public class EuInvoiceGenerator implements InvoiceGenerator {

    @Override
    public Invoice generateInvoice(String transactionId, Customer customer, BigDecimal amount, String currency) {
        BigDecimal rate = vatRateFor(customer.getCountry());
        boolean reverseCharge = customer.getTaxId() != null && !customer.getTaxId().isEmpty();

        BigDecimal tax = reverseCharge ? BigDecimal.ZERO : amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = amount.add(tax);
        String taxLabel = reverseCharge
                ? "VAT reverse-charged (Art. 194-197 EU VAT Directive)"
                : "VAT " + rate.multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString() + "%";

        return new Invoice("EU-INV-" + transactionId, customer.getName(), amount, tax, total, currency, taxLabel);
    }

    private BigDecimal vatRateFor(Country country) {
        switch (country) {
            case GERMANY:
                return new BigDecimal("0.19");
            case FRANCE:
                return new BigDecimal("0.20");
            case SPAIN:
                return new BigDecimal("0.21");
            case ITALY:
                return new BigDecimal("0.22");
            default:
                return new BigDecimal("0.20");
        }
    }
}
