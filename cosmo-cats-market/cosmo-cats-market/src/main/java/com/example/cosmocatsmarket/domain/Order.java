package com.example.cosmocatsmarket.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class Order {
    private UUID id;
    private List<Product> products = new ArrayList<>();
    private Set<UUID> productsId = new HashSet<>(); // All ID must be unique

    private Double productsCoun;
    private Double totalPrice;
}
