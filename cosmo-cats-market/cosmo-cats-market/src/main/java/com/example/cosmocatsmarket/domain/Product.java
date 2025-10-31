package com.example.cosmocatsmarket.domain;

import lombok.*;
import java.util.*;
import java.util.UUID;

@Data
public class Product {
    private UUID productId;
    private String productName;
    private String description;
    private Double price;
    private String review;
    private String status;

    // One product can have more than one categories
    private List<Category> categories = new ArrayList<>();
    //private Set<UUID> categoryIds = new HashSet<>(); // All ID must be unique
    private List<String> categoryIds = new ArrayList<>();

}
