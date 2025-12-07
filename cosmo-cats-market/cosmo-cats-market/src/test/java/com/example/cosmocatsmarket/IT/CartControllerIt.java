package com.example.cosmocatsmarket.IT;

import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.example.cosmocatsmarket.service.CartDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class CartControllerIt extends AbstractWebIt {

    @Autowired
    CartDbService cartDbService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("TRUNCATE TABLE orders CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cart CASCADE");
    }


    @Test
    @DisplayName("GET /api/carts should return empty list")
    void shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    @DisplayName("POST /api/carts should create a cart")
    void shouldCreateCart() throws Exception {
        String body = """
                {
                  "numProduct": 2,
                  "totalPrice": 20.5
                }
                """;

        mockMvc.perform(post("/api/carts")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numProduct").value(2))
                .andExpect(jsonPath("$.totalPrice").value(20.5));
    }

    @Test
    @DisplayName("GET /api/carts/{id} should return existing cart")
    void shouldReturnCartById() throws Exception {
        CartEntity saved = cartDbService.saveCart(
                CartEntity.builder().numProduct(1).totalPrice(9.99).build()
        );

        mockMvc.perform(get("/api/carts/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numProduct").value(1))
                .andExpect(jsonPath("$.totalPrice").value(9.99));
    }

    @Test
    @DisplayName("DELETE /api/carts/{id} should remove cart")
    void shouldDeleteCart() throws Exception {
        CartEntity saved = cartDbService.saveCart(
                CartEntity.builder().numProduct(3).totalPrice(100.0).build()
        );

        mockMvc.perform(delete("/api/carts/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}
