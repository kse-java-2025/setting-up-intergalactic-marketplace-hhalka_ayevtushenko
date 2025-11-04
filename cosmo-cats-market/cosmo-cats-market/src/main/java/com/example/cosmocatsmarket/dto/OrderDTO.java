package com.example.cosmocatsmarket.dto;

import com.example.cosmocatsmarket.validation.CosmoCatAnnotations;
import jakarta.validation.constraints.NotNull;
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
public class OrderDTO {
    private String orderId;
    private String cartId;
    @NotNull(message = "Count of product which you order must be at least 1")
    private List<String> products;
    @NotNull(message = "Null only if products list are empty")
    private List<String> productIds;

    @Min(value = 1, message = "Count of product which you order must be at least 1")
    private Integer productsCount;
    @NotNull(message = "Price cannot be null")
    private Integer totalPrice;
}