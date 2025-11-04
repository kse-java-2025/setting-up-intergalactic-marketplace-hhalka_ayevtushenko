package com.example.cosmocatsmarket.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class Cart {
    private UUID cartId;
    private List<Product> products;
    private Set<UUID> productIds = new HashSet<>(); // All ID must be unique

    private Double productsCount;
    private Double totalPrice;

    public Cart() {
        //
    }
}
