package com.ecommerce.payment.factory;

import com.ecommerce.payment.region.Region;

/**
 * Resolves the correct concrete RegionalPaymentFactory for a detected
 * Region. This is the single place that knows about all concrete
 * factories, keeping the rest of the codebase dependent only on the
 * RegionalPaymentFactory abstraction (Dependency Inversion).
 */
public final class PaymentFactoryProvider {

    private PaymentFactoryProvider() {
    }

    public static RegionalPaymentFactory getFactory(Region region) {
        switch (region) {
            case LATAM:
                return new LATAMPaymentFactory();
            case EU:
                return new EUPaymentFactory();
            case US:
                return new USPaymentFactory();
            default:
                throw new IllegalArgumentException("Unsupported region: " + region);
        }
    }
}
