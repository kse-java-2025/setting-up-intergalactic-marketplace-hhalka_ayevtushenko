package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.dto.ProductDTO;
import com.example.cosmocatsmarket.mapper.ProductMapperImpl;
import com.example.cosmocatsmarket.mapper.ProductMapper;
import com.example.cosmocatsmarket.service.impl.ProductServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


public class ProductServiceImplTest {

    private ProductServiceImpl service;

    @BeforeEach
    void init() {
        ProductMapper mapper = new ProductMapperImpl();
        service = new ProductServiceImpl(mapper);

        ProductDTO dto = new ProductDTO();
        dto.setProductName("Cat Food");
        dto.setPrice(10.5);
        dto.setCategoryIds(List.of("category#1"));
        service.create(dto);
    }

    @Test
    @DisplayName("getAll_returnAtLeastOne: (non empty) will return first element")
    void getAll_returnAtLeastOne() {
        var all = service.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertEquals("Cat Food", all.get(0).getProductName());
    }

    @Test
    @DisplayName("getById_existing_returnsDto: (non empty) get by id")
    void getById_existing_returnsDto() {
        UUID id = service.getAll().get(0).getProductId();
        Optional<ProductDTO> opt_dto = service.getById(id);
        assertTrue(opt_dto.isPresent());
        assertEquals("Cat Food", opt_dto.get().getProductName());
    }

    @Test
    @DisplayName("getById_missingUUID: non-existing ID was not found")
    void getById_missingUUID() {
        Optional<ProductDTO> missing = service.getById(UUID.randomUUID());
        assertTrue(missing.isEmpty());
    }

    @Test
    @DisplayName("create_success_whenNewName: name, categories, price was successfully found")
    void create_success_whenNewName() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Star Helmet");
        dto.setPrice(5.0);
        dto.setCategoryIds(List.of("category#2", "category#3"));

        ProductDTO new_s = service.create(dto);

        assertNotNull(new_s.getProductId());
        assertEquals("Star Helmet", new_s.getProductName());
        assertEquals(5.0, new_s.getPrice());
        assertEquals(List.of("category#2", "category#3"), new_s.getCategoryIds());
    }

    @Test
    @DisplayName("update_existing_returnsUpdated: update product name, price, and category")
    void update_existing_returnsUpdated() {
        UUID id = service.getAll().get(0).getProductId();

        ProductDTO dto = new ProductDTO();
        dto.setProductName("Galaxy Cat Food");
        dto.setPrice(17.0);
        dto.setCategoryIds(List.of("category#9"));

        var updated = service.update(id, dto);
        assertTrue(updated.isPresent());
        assertEquals("Galaxy Cat Food", updated.get().getProductName());
        assertEquals(17.0, updated.get().getPrice());
        assertEquals(List.of("category#9"), updated.get().getCategoryIds());
    }

    @Test
    @DisplayName("update_missing_returnsEmpty: non-existing product can't be update")
    void update_missing_returnsEmpty() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Comet Dust");
        dto.setPrice(0.1);
        dto.setCategoryIds(List.of("category#0", "category#x"));

        var updated = service.update(UUID.randomUUID(), dto);
        assertTrue(updated.isEmpty());
    }
}
