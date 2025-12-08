package com.example.cosmocatsmarket.web;

import com.example.cosmocatsmarket.IT.AbstractIt;
import com.example.cosmocatsmarket.dto.OrderDTO;
import com.example.cosmocatsmarket.repository.CartRepository;
import com.example.cosmocatsmarket.repository.OrderRepository;
import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.example.cosmocatsmarket.repository.entity.OrderEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class OrderControllerIT extends AbstractIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    private String json(Object obj) throws Exception {
        return om.writeValueAsString(obj);
    }

    private CartEntity createCart() {
        return cartRepository.save(
                CartEntity.builder()
                        .numProduct(1)
                        .totalPrice(50.0)
                        .build()
        );
    }

    @Test
    @DisplayName("post_valid_returns201AndPersists: ")
    void post_valid_returns201AndPersists() throws Exception {
        CartEntity cart = createCart();

        OrderDTO req = OrderDTO.builder()
                .cartId(cart.getId().toString())
                .productsCount(1)
                .totalPrice(50)
                .build();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.cartId").value(cart.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(50));
    }

    @Test
    @DisplayName("getById_invalidId_returns400: ")
    void getById_invalidId_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/orders/{orderId}", "not-a-number"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("delete_invalidId_returns400: ")
    void delete_invalidId_returns400() throws Exception {
        mockMvc.perform(delete("/api/v1/orders/{orderId}", "NaN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("patch_notExistingOrder_returns400: ")
    void patch_notExistingOrder_returns400() throws Exception {
        mockMvc.perform(patch("/api/v1/orders/{orderNumber}/status", "ORD-999999")
                        .param("status", "SHIPPED"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getByNumber_existing_returns200: ")
    void getByNumber_existing_returns200() throws Exception {
        CartEntity cart = createCart();

        OrderEntity entity = orderRepository.save(
                OrderEntity.builder()
                        .orderNumber("ORD-42")
                        .cart(cart)
                        .numProduct(1)
                        .totalPrice(100.0)
                        .orderDate(LocalDateTime.now())
                        .status("PENDING")
                        .build()
        );

        mockMvc.perform(get("/api/v1/orders/by-number/{orderNumber}", entity.getOrderNumber()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(entity.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(100));
    }
}
