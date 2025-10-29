package com.example.cosmocatsmarket.domain;

import lombok.*;
import java.util.UUID;

@Data
public class Product {
    private UUID ProductId;
    private String productName;
    private String description;
    private Double price;
    private String review;
    private String status;

    private String categoryName;
    private UUID categoryId;
}
