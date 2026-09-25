package com.ecommerce.payment.web;

import com.ecommerce.payment.service.PaymentOrchestrationResult;
import com.ecommerce.payment.service.PaymentOrchestrator;
import com.ecommerce.payment.service.PaymentRequest;
import com.ecommerce.payment.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class PaymentHttpServer {

    private final int port;
    private final PaymentOrchestrator orchestrator = new PaymentOrchestrator();

    public PaymentHttpServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new FrontendHandler());
        server.createContext("/api/pay", new PayHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Payment platform demo listening on http://localhost:" + port);
    }

    private static class FrontendHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            byte[] body = FrontendContent.HTML.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        }
    }

    private class PayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            try {
                String requestBody = readBody(exchange.getRequestBody());
                Map<String, String> fields = JsonUtil.parseFlatObject(requestBody);

                PaymentRequest paymentRequest = new PaymentRequest(
                        fields.get("country"),
                        fields.get("paymentType"),
                        new BigDecimal(fields.get("amount")),
                        fields.get("currency"),
                        fields.getOrDefault("customerId", "CUST-ANONYMOUS"),
                        fields.get("customerName"),
                        fields.get("customerEmail"),
                        fields.get("customerTaxId"),
                        fields.get("cardLast4"),
                        fields.get("subscriptionId"));

                PaymentOrchestrationResult result = orchestrator.processPayment(paymentRequest);
                sendJson(exchange, 200, JsonUtil.toJson(toResponseMap(result)));

            } catch (Exception e) {
                Map<String, Object> error = new LinkedHashMap<>();
                error.put("error", e.getClass().getSimpleName() + ": " + e.getMessage());
                sendJson(exchange, 400, JsonUtil.toJson(error));
            }
        }
    }

    private static Map<String, Object> toResponseMap(PaymentOrchestrationResult result) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("region", result.getRegion().name());
        response.put("factoryUsed", result.getFactoryUsed());
        response.put("gatewayUsed", result.getGatewayUsed());
        response.put("transactionTypeUsed", result.getTransactionTypeUsed());

        Map<String, Object> fraudMap = new LinkedHashMap<>();
        fraudMap.put("passed", result.getFraudCheckResult().isPassed());
        fraudMap.put("riskScore", result.getFraudCheckResult().getRiskScore());
        fraudMap.put("reason", result.getFraudCheckResult().getReason());
        response.put("fraudCheck", fraudMap);

        if (result.getTransactionResult() != null) {
            Map<String, Object> txMap = new LinkedHashMap<>();
            txMap.put("success", result.getTransactionResult().isSuccess());
            txMap.put("transactionId", result.getTransactionResult().getTransactionId());
            txMap.put("attempts", result.getTransactionResult().getAttempts());
            txMap.put("message", result.getTransactionResult().getMessage());
            response.put("transaction", txMap);
        }

        if (result.getInvoice() != null) {
            Map<String, Object> invoiceMap = new LinkedHashMap<>();
            invoiceMap.put("invoiceId", result.getInvoice().getInvoiceId());
            invoiceMap.put("subtotal", result.getInvoice().getSubtotal());
            invoiceMap.put("taxAmount", result.getInvoice().getTaxAmount());
            invoiceMap.put("total", result.getInvoice().getTotal());
            invoiceMap.put("currency", result.getInvoice().getCurrency());
            invoiceMap.put("taxLabel", result.getInvoice().getTaxLabel());
            response.put("invoice", invoiceMap);
        }

        return response;
    }

    private static String readBody(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(chunk)) != -1) {
            buffer.write(chunk, 0, bytesRead);
        }
        return buffer.toString(StandardCharsets.UTF_8.name());
    }

    private static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }
}
