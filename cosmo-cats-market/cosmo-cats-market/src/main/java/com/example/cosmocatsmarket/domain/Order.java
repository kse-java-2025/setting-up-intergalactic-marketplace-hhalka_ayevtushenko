package com.example.cosmocatsmarket.domain;

import lombok.*;
import java.util.UUID;

@Data
public class Order {
    private UUID id;
    private List<Product> products = new ArrayList<>();

    private Double totalPrice;
}
