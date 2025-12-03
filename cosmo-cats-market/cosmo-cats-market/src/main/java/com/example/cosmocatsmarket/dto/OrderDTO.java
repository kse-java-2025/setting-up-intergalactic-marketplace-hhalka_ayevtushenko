package com.example.cosmocatsmarket.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
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

    @Builder.Default
    private List<String> products = List.of();

    @Builder.Default
    private List<String> productIds = List.of();

    @Min(value = 1, message = "Count of product which you order must be at least 1")
    private Integer productsCount;

    @NotNull(message = "Price cannot be null")
    private Integer totalPrice;
}
