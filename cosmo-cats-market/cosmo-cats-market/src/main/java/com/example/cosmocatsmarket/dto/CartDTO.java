package com.example.cosmocatsmarket.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private UUID cartId;

    @NotNull
    @NotEmpty
    private List<String> productIds;
    private Double productsCount;

    @NotNull(message = "Price cannot be null")
    private Double totalPrice;
}
