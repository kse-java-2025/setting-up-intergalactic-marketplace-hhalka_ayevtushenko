package com.example.cosmocatsmarket.domain;

import com.example.cosmocatsmarket.domain.Category;
import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Value
@Builder(toBuilder = true)
public class Product {
    UUID productId;
    String productName;
    String description;
    Double price;
    String review;
    String status;
    List<Category> categories;
    List<String> categoryIds;

    public Product(
            UUID productId,
            String productName,
            String description,
            Double price,
            String review,
            String status,
            List<Category> categories,
            List<String> categoryIds
    ) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.review = review;
        this.status = status;

        this.categories = categories != null ? Collections.unmodifiableList(categories) : Collections.emptyList();
        this.categoryIds = categoryIds != null ? Collections.unmodifiableList(categoryIds) : Collections.emptyList();
    }
}
