package com.example.cosmocatsmarket.web;

import com.example.cosmocatsmarket.IT.AbstractIt;
import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
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
public class CategoryControllerIT extends AbstractIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategoryRepository categoryRepository;

    private String json(Object obj) throws Exception {
        return om.writeValueAsString(obj);
    }

    @Test
    @DisplayName("getAll_returns200AndNonEmptyList: ")
    void getAll_returns200AndNonEmptyList() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("getById_notExisting_returns404: ")
    void getById_notExisting_returns404() throws Exception {
        mockMvc.perform(get("/api/categories/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("post_valid_returns200AndPersists: ")
    void post_valid_returns200AndPersists() throws Exception {
        CategoryEntity req = CategoryEntity.builder()
                .name("Space Toys")
                .build();

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Space Toys"));
    }

    @Test
    @DisplayName("getByName_existing_returns200: ")
    void getByName_existing_returns200() throws Exception {
        CategoryEntity entity = categoryRepository.save(
                CategoryEntity.builder()
                        .name("Bowls")
                        .build()
        );

        mockMvc.perform(get("/api/categories/name/{name}", entity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.name").value("Bowls"));
    }

    @Test
    @DisplayName("put_notExisting_returns400: ")
    void put_notExisting_returns400() throws Exception {
        CategoryEntity req = CategoryEntity.builder()
                .name("Updated")
                .build();

        mockMvc.perform(put("/api/categories/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("delete_existing_returns204: ")
    void delete_existing_returns204() throws Exception {
        CategoryEntity entity = categoryRepository.save(
                CategoryEntity.builder()
                        .name("ToDelete")
                        .build()
        );

        mockMvc.perform(delete("/api/categories/{id}", entity.getId()))
                .andExpect(status().isNoContent());
    }
}
