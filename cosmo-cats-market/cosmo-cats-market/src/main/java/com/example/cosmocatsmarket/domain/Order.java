package com.example.cosmocatsmarket.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class Order {
    private UUID orderId;
    private UUID cartId;
    private List<Product> products = new ArrayList<>();
    private Set<UUID> productsId = new HashSet<>();

    private Double productsCount;
    private Double totalPrice;
}
