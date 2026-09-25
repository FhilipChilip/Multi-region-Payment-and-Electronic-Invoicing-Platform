package com.ecommerce.payment.region;

import java.util.HashMap;
import java.util.Map;

/**
 * Countries supported by this demo, each associated with the macro-region
 * it belongs to. In a production system this catalogue would likely be
 * data-driven, but a fixed enum keeps the sample self-contained and easy
 * to compile and run.
 */
public enum Country {
    COLOMBIA("CO", "Colombia", Region.LATAM),
    MEXICO("MX", "Mexico", Region.LATAM),
    BRAZIL("BR", "Brazil", Region.LATAM),
    ARGENTINA("AR", "Argentina", Region.LATAM),
    GERMANY("DE", "Germany", Region.EU),
    FRANCE("FR", "France", Region.EU),
    SPAIN("ES", "Spain", Region.EU),
    ITALY("IT", "Italy", Region.EU),
    UNITED_STATES("US", "United States", Region.US);

    private final String isoCode;
    private final String displayName;
    private final Region region;

    Country(String isoCode, String displayName, Region region) {
        this.isoCode = isoCode;
        this.displayName = displayName;
        this.region = region;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Region getRegion() {
        return region;
    }

    private static final Map<String, Country> BY_ISO_CODE = new HashMap<>();

    static {
        for (Country country : values()) {
            BY_ISO_CODE.put(country.isoCode, country);
        }
    }

    public static Country fromIsoCode(String isoCode) {
        Country country = BY_ISO_CODE.get(isoCode == null ? null : isoCode.trim().toUpperCase());
        if (country == null) {
            throw new IllegalArgumentException("Unsupported country code: " + isoCode);
        }
        return country;
    }
}
