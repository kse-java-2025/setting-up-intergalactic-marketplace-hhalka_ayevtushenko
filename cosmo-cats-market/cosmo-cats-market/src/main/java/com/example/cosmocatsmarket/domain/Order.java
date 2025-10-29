package com.example.cosmocatsmarket.domain;

import lombok.Builder;

public class Order {
    String id;
    List<Product> products;
    String cartId;
    Double totalPrice;
}
