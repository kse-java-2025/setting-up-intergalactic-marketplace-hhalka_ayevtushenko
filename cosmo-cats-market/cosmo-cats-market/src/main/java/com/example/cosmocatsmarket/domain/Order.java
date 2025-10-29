package com.example.cosmocatsmarket.domain;

import lombok.*;
import java.util.UUID;

@Data
public class Order {
    private UUID id;
    private List<Product> products = new ArrayList<>();
    private Set<UUID> productsId = new HashSet<>(); // All ID must be unique

    private Double productsCoun;
    private Double totalPrice;
}
