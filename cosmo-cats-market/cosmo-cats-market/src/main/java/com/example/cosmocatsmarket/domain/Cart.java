package com.example.cosmocatsmarket.domain;

import java.util.List;
import lombok.*;

@Data
public class Cart {
    private List<Product> products;
    private Double totalPrice;

    public Cart() {
        //
    }
}
