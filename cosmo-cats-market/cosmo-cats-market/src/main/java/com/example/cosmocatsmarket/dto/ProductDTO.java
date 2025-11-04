package com.example.cosmocatsmarket.dto;

import com.example.cosmocatsmarket.validation.CosmoCatAnnotations;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private String productId;
    private String status;
    private String description;
    private String review;

    @Size(min = 1, message = "Product name must have at least 1 symbol")
    @CosmoCatAnnotations // Added Validation
    private String productName;

    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @Size(min = 1, message = "Product must have at least one category")
    private List<String> categoryIds;
}
