package com.example.cosmocatsmarket.dto;

import java.util.List;
import lombok.*;
import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private String productId;
    private String status;
    private String description;
    private String review;

    @Size(min = 1, message = "Product name must have at least 1 symbvol")
    private String productName;

    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @Size(min = 1, message = "Product must have at least one category")
    private List<String> categoryIds;
}
