package com.example.cosmocatsmarket.IT;

import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.example.cosmocatsmarket.repository.entity.OrderEntity;
import com.example.cosmocatsmarket.service.CartDbService;
import com.example.cosmocatsmarket.service.OrderDbService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDbServiceImplIt extends AbstractIt {

    @Autowired
    private OrderDbService orderDbService;

    @Autowired
    private CartDbService cartDbService;

    @Test
    @DisplayName("shouldCreateAndReadOrderFromDb: Create order and read it by id")
    void shouldCreateAndReadOrderFromDb() {
        CartEntity cart = CartEntity.builder()
                .numProduct(2)
                .totalPrice(50.0)
                .build();
        CartEntity savedCart = cartDbService.saveCart(cart);

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-1");
        order.setCustomerEmail("space.cat@example.com");
        order.setTotalPrice(10.5);
        order.setNumProduct(2);
        order.setCart(savedCart);

        OrderEntity savedOrder = orderDbService.saveOrder(order);

        assertNotNull(savedOrder.getId());

        Optional<OrderEntity> foundOpt = orderDbService.findOrderById(savedOrder.getId());
        assertTrue(foundOpt.isPresent());
        OrderEntity found = foundOpt.get();

        assertEquals("ORD-1", found.getOrderNumber());
        assertEquals("space.cat@example.com", found.getCustomerEmail());
        assertEquals(10.5, found.getTotalPrice());
        assertEquals(2, found.getNumProduct());

        Optional<OrderEntity> byNaturalId = orderDbService.findOrderByNaturalId("ORD-1");
        assertTrue(byNaturalId.isPresent());
        assertEquals(savedOrder.getId(), byNaturalId.get().getId());
    }
}
