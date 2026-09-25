package com.ecommerce.payment.factory;

import com.ecommerce.payment.fraud.FraudValidator;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.ecommerce.payment.invoice.InvoiceGenerator;

/**
 * ABSTRACT FACTORY PATTERN.
 *
 * Declares the family of region-specific objects that must be created
 * together and stay mutually consistent: the payment gateway (a Bridge
 * Implementor, always an Adapter instance), the tax-compliant invoice
 * generator, and the fraud validator that enforces that region's rules.
 */
public interface RegionalPaymentFactory {

    PaymentGateway createPaymentGateway();

    InvoiceGenerator createInvoiceGenerator();

    FraudValidator createFraudValidator();
}
