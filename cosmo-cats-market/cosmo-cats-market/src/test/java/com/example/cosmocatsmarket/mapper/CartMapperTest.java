package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Cart;
import com.example.cosmocatsmarket.dto.CartDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartMapperTest {
    private final CartMapper mapper = Mappers.getMapper(CartMapper.class);

    @Test
    @DisplayName("shouldMapCartToDto: Map cart to DTO")
    void shouldMapCartToDto() {

    }

    @Test
    @DisplayName("shouldMapDtoToCart: Map DTO to cart")
    void shouldMapDtoToCart() {

    }
}
