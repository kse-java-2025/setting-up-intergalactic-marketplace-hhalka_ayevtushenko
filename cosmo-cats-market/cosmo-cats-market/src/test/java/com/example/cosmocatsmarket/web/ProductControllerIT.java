package com.example.cosmocatsmarket.web;

import com.example.cosmocatsmarket.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import lombok.SneakyThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @SneakyThrows
    private String json(Object obj) {
        return om.writeValueAsString(obj);
    }

    @Test
    @SneakyThrows
    @DisplayName("post_invalid_emptyName_returns400: Name must be given")
    void post_invalid_emptyName_returns400() {
        ProductDTO req = new ProductDTO();
        req.setProductName("");
        req.setPrice(1.0);
        req.setCategoryIds(List.of("category#1"));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("post_invalid_nullPrice_returns400: Price must be given")
    void post_invalid_nullPrice_returns400() {
        ProductDTO req = new ProductDTO();
        req.setProductName("Galaxy Bowl");
        req.setPrice(null);
        req.setCategoryIds(List.of("category#2"));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("post_invalid_negativePrice_returns400: Price must be more or equal 0")
    void post_invalid_negativePrice_returns400() {
        ProductDTO req = new ProductDTO();
        req.setProductName("Galaxy Bowl");
        req.setPrice(-1.0);
        req.setCategoryIds(List.of("category#3"));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("post_invalid_emptyCategories_returns400: All products must have at leas one category")
    void post_invalid_emptyCategories_returns400() {
        ProductDTO req = new ProductDTO();
        req.setProductName("Galaxy Bowl");
        req.setPrice(1.0);
        req.setCategoryIds(List.of());

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("put_invalid_uuidPath_returns400: Invalid UUID")
    void put_invalid_uuidPath_returns400() {
        ProductDTO req = new ProductDTO();
        req.setProductName("Galaxy Bowl");
        req.setPrice(1.0);
        req.setCategoryIds(List.of("category#4"));

        mockMvc.perform(put("/api/v1/products/not-a-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)));
    }
}

