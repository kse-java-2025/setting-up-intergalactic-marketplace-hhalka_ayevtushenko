package com.example.cosmocatsmarket.dto;

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
    private List<String> productIds;
    private Integer productsCount;
    private Double totalPrice;
}
