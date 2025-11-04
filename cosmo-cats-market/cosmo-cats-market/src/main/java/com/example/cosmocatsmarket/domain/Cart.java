package com.example.cosmocatsmarket.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Cart {
    private UUID cartId;
    private List<Product> products = new ArrayList<>();
    private List<String> productIds = new ArrayList<>();
    private Double productsCount;
    private Double totalPrice;
}
