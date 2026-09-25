package com.ecommerce.payment.service;

import com.ecommerce.payment.factory.PaymentFactoryProvider;
import com.ecommerce.payment.factory.RegionalPaymentFactory;
import com.ecommerce.payment.fraud.FraudCheckResult;
import com.ecommerce.payment.fraud.FraudValidator;
import com.ecommerce.payment.gateway.ChargeRequest;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.ecommerce.payment.invoice.Invoice;
import com.ecommerce.payment.invoice.InvoiceGenerator;
import com.ecommerce.payment.model.Customer;
import com.ecommerce.payment.region.Country;
import com.ecommerce.payment.region.Region;
import com.ecommerce.payment.region.RegionDetector;
import com.ecommerce.payment.transaction.OneTimePayment;
import com.ecommerce.payment.transaction.PaymentTransaction;
import com.ecommerce.payment.transaction.SubscriptionPayment;
import com.ecommerce.payment.transaction.TransactionResult;

/**
 * Coordinates the full "integrated flow" described in the case study:
 *
 *   1. Detect the region for the transaction's country.
 *   2. Ask PaymentFactoryProvider for that region's RegionalPaymentFactory
 *      (ABSTRACT FACTORY PATTERN) and use it to create a matched gateway,
 *      invoice generator and fraud validator.
 *   3. Wrap the request in the right PaymentTransaction refinement
 *      (BRIDGE PATTERN abstraction: SubscriptionPayment or OneTimePayment),
 *      injected with the gateway the factory produced - an ADAPTER
 *      PATTERN instance acting as the Bridge implementor.
 *   4. Run the fraud check, execute the charge, and - if successful -
 *      generate a region-compliant invoice.
 *
 * This class is the composition root that ties Abstract Factory, Bridge
 * and Adapter together; none of the three patterns needs to know about
 * the other two.
 */
public class PaymentOrchestrator {

    private static final int SUBSCRIPTION_MAX_ATTEMPTS = 3;

    public PaymentOrchestrationResult processPayment(PaymentRequest request) {
        Country country = Country.fromIsoCode(request.getCountryIsoCode());
        Region region = RegionDetector.detect(country);

        RegionalPaymentFactory factory = PaymentFactoryProvider.getFactory(region);
        PaymentGateway gateway = factory.createPaymentGateway();
        FraudValidator fraudValidator = factory.createFraudValidator();
        InvoiceGenerator invoiceGenerator = factory.createInvoiceGenerator();

        Customer customer = new Customer(request.getCustomerId(), request.getCustomerName(),
                request.getCustomerEmail(), country, request.getCustomerTaxId());

        ChargeRequest fraudCheckRequest = new ChargeRequest(request.getAmount(), request.getCurrency(),
                customer.getId(), customer.getEmail(), request.getCardLast4(), "Fraud pre-check");
        FraudCheckResult fraudCheckResult = fraudValidator.validate(fraudCheckRequest);

        if (!fraudCheckResult.isPassed()) {
            return new PaymentOrchestrationResult(region, factory.getClass().getSimpleName(),
                    gateway.getGatewayName(), request.getPaymentType(), fraudCheckResult, null, null);
        }

        PaymentTransaction transaction = buildTransaction(request, gateway);
        TransactionResult transactionResult = transaction.charge();

        Invoice invoice = null;
        if (transactionResult.isSuccess()) {
            invoice = invoiceGenerator.generateInvoice(transactionResult.getTransactionId(), customer,
                    request.getAmount(), request.getCurrency());
        }

        return new PaymentOrchestrationResult(region, factory.getClass().getSimpleName(),
                gateway.getGatewayName(), request.getPaymentType(), fraudCheckResult, transactionResult, invoice);
    }

    private PaymentTransaction buildTransaction(PaymentRequest request, PaymentGateway gateway) {
        if ("SUBSCRIPTION".equalsIgnoreCase(request.getPaymentType())) {
            return new SubscriptionPayment(gateway, request.getAmount(), request.getCurrency(),
                    request.getCustomerId(), request.getCustomerEmail(), request.getCardLast4(),
                    request.getSubscriptionId(), SUBSCRIPTION_MAX_ATTEMPTS);
        }
        return new OneTimePayment(gateway, request.getAmount(), request.getCurrency(),
                request.getCustomerId(), request.getCustomerEmail(), request.getCardLast4());
    }
}
