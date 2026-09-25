package com.ecommerce.payment.factory;

import com.ecommerce.payment.adapter.MercadoPagoAdapter;
import com.ecommerce.payment.fraud.FraudValidator;
import com.ecommerce.payment.fraud.LatamFraudValidator;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.ecommerce.payment.invoice.InvoiceGenerator;
import com.ecommerce.payment.invoice.LatamInvoiceGenerator;

public class LATAMPaymentFactory implements RegionalPaymentFactory {

    @Override
    public PaymentGateway createPaymentGateway() {
        return new MercadoPagoAdapter();
    }

    @Override
    public InvoiceGenerator createInvoiceGenerator() {
        return new LatamInvoiceGenerator();
    }

    @Override
    public FraudValidator createFraudValidator() {
        return new LatamFraudValidator();
    }
}
