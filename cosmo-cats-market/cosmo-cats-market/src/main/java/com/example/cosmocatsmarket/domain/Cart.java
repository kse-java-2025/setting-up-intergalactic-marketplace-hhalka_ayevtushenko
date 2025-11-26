package com.example.cosmocatsmarket.domain;

import lombok.Value;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Value
public class Cart {
    UUID cartId;
    List<Product> products;
    List<String> productIds;
    Double productsCount;
    Double totalPrice;

    public Cart(UUID cartId,
                List<Product> products,
                List<String> productIds,
                Double productsCount,
                Double totalPrice) {

        this.cartId = cartId;
        this.products = products != null
                ? Collections.unmodifiableList(products)
                : Collections.emptyList();


        this.productIds = productIds != null
                ? Collections.unmodifiableList(productIds)
                : Collections.emptyList();


        this.productsCount = productsCount;
        this.totalPrice = totalPrice;
    }
}
