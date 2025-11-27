package com.example.cosmocatsmarket.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Product {
    private UUID productId;
    private String productName;
    private String description;
    private Double price;
    private String review;
    private String status;

    private List<Category> categories = new ArrayList<>();
    private List<String> categoryIds = new ArrayList<>();

}
