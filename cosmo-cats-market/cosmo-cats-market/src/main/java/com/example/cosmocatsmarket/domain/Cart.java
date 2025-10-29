package com.example.cosmocatsmarket.domain;

import java.util.List;
import lombok.*;

@Data
public class Cart {
    private List<Product> products;
    private Set<UUID> productsId = new HashSet<>(); // All ID must be unique

    private Double productsCoun;
    private Double totalPrice;

    public Cart() {
        //
    }
}
