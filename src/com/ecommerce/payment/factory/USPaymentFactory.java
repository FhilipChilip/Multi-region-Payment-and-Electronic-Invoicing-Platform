package com.ecommerce.payment.factory;

import com.ecommerce.payment.adapter.StripeAdapter;
import com.ecommerce.payment.fraud.FraudValidator;
import com.ecommerce.payment.fraud.UsFraudValidator;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.ecommerce.payment.invoice.InvoiceGenerator;
import com.ecommerce.payment.invoice.UsInvoiceGenerator;

public class USPaymentFactory implements RegionalPaymentFactory {

    @Override
    public PaymentGateway createPaymentGateway() {
        return new StripeAdapter();
    }

    @Override
    public InvoiceGenerator createInvoiceGenerator() {
        return new UsInvoiceGenerator();
    }

    @Override
    public FraudValidator createFraudValidator() {
        return new UsFraudValidator();
    }
}
