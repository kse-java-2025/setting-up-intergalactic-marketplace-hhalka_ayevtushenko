package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Order;
import com.example.cosmocatsmarket.dto.OrderDTO;
import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrderMapperTest {
    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Test
    @DisplayName("shouldMapOrderToDto: Map order to DTO")
    void shouldMapOrderToDto() {
        Order order = new Order();

        UUID orderId = UUID.fromString("aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb");
        UUID cartId  = UUID.fromString("cccccccc-4444-5555-6666-dddddddddddd");
        order.setOrderId(orderId);
        order.setCartId(cartId);

        Product product1 = new Product();
        UUID productId1 = UUID.fromString("11111111-2222-3333-4444-555555555555");
        product1.setProductId(productId1);

        Product product2 = new Product();
        UUID productId2 = UUID.fromString("66666666-7777-8888-9999-000000000000");
        product2.setProductId(productId2);

        order.setProducts(List.of(product1, product2));
        order.setProductsCount(2.0);
        order.setTotalPrice(100.0);
        OrderDTO dto = mapper.toDto(order);

        assertNotNull(dto);
        assertEquals(orderId.toString(), dto.getOrderId());
        assertEquals(cartId.toString(), dto.getCartId());
        assertNotNull(dto.getProducts());
        assertEquals(2, dto.getProducts().size());
        assertEquals(productId1.toString(), dto.getProducts().get(0));
        assertEquals(productId2.toString(), dto.getProducts().get(1));
        assertEquals(order.getProductsCount().intValue(), dto.getProductsCount());
        assertEquals(order.getTotalPrice().intValue(), dto.getTotalPrice());
        assertNotNull(dto.getProductIds());
        assertTrue(dto.getProductIds().isEmpty());
    }

    @Test
    @DisplayName("shouldMapDtoToOrder: Map DTO to order")
    void shouldMapDtoToOrder() {
        UUID cartId = UUID.fromString("cccccccc-4444-5555-6666-dddddddddddd");
        String productId1 = "11111111-2222-3333-4444-555555555555";
        String productId2 = "66666666-7777-8888-9999-000000000000";
        OrderDTO dto = OrderDTO.builder()
                .orderId("ignored-order-id")
                .cartId(cartId.toString())
                .products(List.of(productId1, productId2))
                .productsCount(2)
                .totalPrice(100)
                .build();
        Order order = mapper.fromDTO(dto);

        assertNotNull(order);
        assertEquals(cartId, order.getCartId());
        assertNotNull(order.getProducts());
        assertEquals(2, order.getProducts().size());
        assertEquals(UUID.fromString(productId1), order.getProducts().get(0).getProductId());
        assertEquals(UUID.fromString(productId2), order.getProducts().get(1).getProductId());
        assertEquals(dto.getProductsCount().doubleValue(), order.getProductsCount());
        assertEquals(dto.getTotalPrice().doubleValue(), order.getTotalPrice());
    }
}
