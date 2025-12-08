package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Order;
import com.example.cosmocatsmarket.dto.OrderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrderMapperTest {
    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Test
    @DisplayName("shouldMapOrderToDto: Map order to DTO")
    void shouldMapOrderToDto() {

    }

    @Test
    @DisplayName("shouldMapDtoToOrder: Map DTO to order")
    void shouldMapDtoToOrder() {

    }
}
