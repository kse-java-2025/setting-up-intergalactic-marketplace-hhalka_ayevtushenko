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
public class CartDTO {
    private String cartId;
    private List<String> products;
    private List<String> categoryIds;

    private Integer productsCount;
    @NotNull(message = "Price cannot be null")
    private Integer totalPrice;
}