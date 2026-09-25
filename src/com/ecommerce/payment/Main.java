package com.ecommerce.payment;

import com.ecommerce.payment.factory.LATAMPaymentFactory;
import com.ecommerce.payment.factory.RegionalPaymentFactory;
import com.ecommerce.payment.gateway.PaymentGateway;
import com.ecommerce.payment.invoice.Invoice;
import com.ecommerce.payment.invoice.InvoiceGenerator;
import com.ecommerce.payment.model.Customer;
import com.ecommerce.payment.region.Country;
import com.ecommerce.payment.transaction.SubscriptionPayment;
import com.ecommerce.payment.transaction.TransactionResult;
import com.ecommerce.payment.web.PaymentHttpServer;

import java.math.BigDecimal;

/**
 * Entry point.
 *
 * First runs, on the console, the exact scenario described in the case
 * study's "integrated flow": a subscription payment from Colombia, routed
 * through MercadoPago via the LATAM factory. Then starts the small HTTP
 * server that exposes the same engine to the HTML front-end, so any
 * country / payment-type combination can be tried interactively.
 */
public final class Main {

    private static final int HTTP_PORT = 8080;

    public static void main(String[] args) throws Exception {
        runCaseStudyScenario();

        new PaymentHttpServer(HTTP_PORT).start();
        System.out.println("Open http://localhost:" + HTTP_PORT + " in your browser to try other scenarios.");
    }

    private static void runCaseStudyScenario() {
        System.out.println("=== Case study scenario: subscription payment from Colombia ===");

        // Step 1: the system detects that the transaction happens in Colombia
        // and instantiates the LATAM concrete factory (ABSTRACT FACTORY).
        Country country = Country.COLOMBIA;
        RegionalPaymentFactory factory = new LATAMPaymentFactory();
        System.out.println("Detected country: " + country.getDisplayName() + " -> region: " + country.getRegion());
        System.out.println("Instantiated factory: " + factory.getClass().getSimpleName());

        // Step 2: the factory creates the gateway (an ADAPTER wrapping the
        // MercadoPago SDK) and the invoice generator for the region.
        PaymentGateway gateway = factory.createPaymentGateway();
        InvoiceGenerator invoiceGenerator = factory.createInvoiceGenerator();
        System.out.println("Gateway created by the factory: " + gateway.getGatewayName()
                + " (" + gateway.getClass().getSimpleName() + ")");

        // Step 3: a SubscriptionPayment (BRIDGE abstraction) is created and
        // injected with the gateway (BRIDGE implementor).
        SubscriptionPayment subscription = new SubscriptionPayment(
                gateway,
                new BigDecimal("29.99"),
                "COP",
                "CUST-001",
                "customer.colombia@example.com",
                "4242",
                "SUB-COL-001",
                3);
        System.out.println("Created SubscriptionPayment bound to " + gateway.getGatewayName());

        // Step 4: the client calls subscription.charge(); the abstraction
        // applies its retry policy and delegates to the adapter, which
        // translates the call into MercadoPago's own API.
        TransactionResult result = subscription.charge();
        System.out.println("Charge result: " + (result.isSuccess() ? "SUCCESS" : "FAILED")
                + " after " + result.getAttempts() + " attempt(s) - " + result.getMessage());

        if (result.isSuccess()) {
            Customer customer = new Customer("CUST-001", "Camila Restrepo",
                    "customer.colombia@example.com", country, null);
            Invoice invoice = invoiceGenerator.generateInvoice(result.getTransactionId(), customer,
                    new BigDecimal("29.99"), "COP");
            System.out.println("Invoice generated: " + invoice.getInvoiceId() + " total "
                    + invoice.getTotal() + " " + invoice.getCurrency() + " (" + invoice.getTaxLabel() + ")");
        }

        System.out.println("=== End of case study scenario ===" + System.lineSeparator());
    }
}
