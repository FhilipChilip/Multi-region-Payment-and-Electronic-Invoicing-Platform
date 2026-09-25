package com.ecommerce.payment.region;

public final class RegionDetector {

    private RegionDetector() {
    }

    public static Region detect(Country country) {
        if (country == null) {
            throw new IllegalArgumentException("Country must not be null");
        }
        return country.getRegion();
    }

    public static Region detectByIsoCode(String countryIsoCode) {
        return detect(Country.fromIsoCode(countryIsoCode));
    }
}
