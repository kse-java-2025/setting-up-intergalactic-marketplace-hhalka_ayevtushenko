package com.example.cosmocatsmarket.web;

import com.example.cosmocatsmarket.IT.AbstractIt;
import com.example.cosmocatsmarket.repository.CartRepository;
import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CartControllerIT extends AbstractIt {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CartRepository cartRepository;

    private String json(Object obj) throws Exception {
        return om.writeValueAsString(obj);
    }

    @Test
    @DisplayName("getAll_returns200AndNonEmptyList: ")
    void getAll_returns200AndNonEmptyList() throws Exception {
        mockMvc.perform(get("/api/carts").with(withApiKey()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("getById_notExisting_returns404: ")
    void getById_notExisting_returns404() throws Exception {
        mockMvc.perform(get("/api/carts/{id}", 9999L).with(withApiKey()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("post_valid_returns200AndPersists: ")
    void post_valid_returns200AndPersists() throws Exception {
        CartEntity req = CartEntity.builder()
                .numProduct(2)
                .totalPrice(100.0)
                .build();

        mockMvc.perform(post("/api/carts").with(withApiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.numProduct").value(2))
                .andExpect(jsonPath("$.totalPrice").value(100.0));
    }

    @Test
    @DisplayName("put_notExisting_returns400: ")
    void put_notExisting_returns400() throws Exception {
        CartEntity req = CartEntity.builder()
                .numProduct(3)
                .totalPrice(200.0)
                .build();

        mockMvc.perform(put("/api/carts/{id}", 9999L).with(withApiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("delete_existing_returns204: ")
    void delete_existing_returns204() throws Exception {
        CartEntity cart = cartRepository.save(
                CartEntity.builder()
                        .numProduct(1)
                        .totalPrice(50.0)
                        .build()
        );

        mockMvc.perform(delete("/api/carts/{id}", cart.getId()).with(withApiKey()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("getById_existing_returns200AndBody: ")
    void getById_existing_returns200AndBody() throws Exception {
        CartEntity cart = cartRepository.save(
                CartEntity.builder()
                        .numProduct(3)
                        .totalPrice(150.0)
                        .build()
        );

        mockMvc.perform(get("/api/carts/{id}", cart.getId()).with(withApiKey()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cart.getId()))
                .andExpect(jsonPath("$.numProduct").value(3))
                .andExpect(jsonPath("$.totalPrice").value(150.0));
    }

    @Test
    @DisplayName("put_existing_returns200AndUpdates: ")
    void put_existing_returns200AndUpdates() throws Exception {
        CartEntity existing = cartRepository.save(
                CartEntity.builder()
                        .numProduct(1)
                        .totalPrice(50.0)
                        .build()
        );

        CartEntity req = CartEntity.builder()
                .numProduct(5)
                .totalPrice(250.0)
                .build();

        mockMvc.perform(put("/api/carts/{id}", existing.getId()).with(withApiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existing.getId()))
                .andExpect(jsonPath("$.numProduct").value(5))
                .andExpect(jsonPath("$.totalPrice").value(250.0));

        CartEntity updated = cartRepository.findById(existing.getId()).orElseThrow();
        org.assertj.core.api.Assertions.assertThat(updated.getNumProduct()).isEqualTo(5);
        org.assertj.core.api.Assertions.assertThat(updated.getTotalPrice()).isEqualTo(250.0);
    }
}
