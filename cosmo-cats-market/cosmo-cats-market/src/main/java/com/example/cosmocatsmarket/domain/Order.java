package com.example.cosmocatsmarket.domain;

import lombok.Builder;
import lombok.Value;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Value
@Builder
public class Order {
    UUID orderId;
    UUID cartId;
    List<Product> products;
    Set<UUID> productsId;
    Double productsCount;
    Double totalPrice;

    public Order(
            UUID orderId,
            UUID cartId,
            List<Product> products,
            Set<UUID> productsId,
            Double productsCount,
            Double totalPrice
    ) {
        this.orderId = orderId;
        this.cartId = cartId;

        this.products = products != null
                ? Collections.unmodifiableList(products)
                : Collections.emptyList();

        this.productsId = productsId != null
                ? Collections.unmodifiableSet(productsId)
                : Collections.emptySet();

        this.productsCount = productsCount;
        this.totalPrice = totalPrice;
    }
}
