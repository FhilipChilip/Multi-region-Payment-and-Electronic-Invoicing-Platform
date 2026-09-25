package com.ecommerce.payment.model;

import com.ecommerce.payment.region.Country;

/** Minimal customer record used to drive invoicing and fraud checks. */
public final class Customer {

    private final String id;
    private final String name;
    private final String email;
    private final Country country;
    private final String taxId; // optional: VAT number, NIT, RFC, etc.

    public Customer(String id, String name, String email, Country country, String taxId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country = country;
        this.taxId = taxId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Country getCountry() {
        return country;
    }

    public String getTaxId() {
        return taxId;
    }
}
