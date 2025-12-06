package com.example.cosmocatsmarket.IT;

import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.repository.ProductRepository;
import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import com.example.cosmocatsmarket.repository.entity.ProductEntity;
import com.example.cosmocatsmarket.service.ProductDbService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDbServiceImplIt extends AbstractIt {

    @Autowired
    private ProductDbService productDbService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void postgresContainerIsRunning() {
        assertTrue(postgres.isRunning(), "Postgres Testcontainer is not running");
        System.out.println("JDBC URL = " + postgres.getJdbcUrl());
    }

    @Test
    @DisplayName("shouldCreateAndReadProductFromDb: Create product and read it")
    void shouldCreateAndReadProductFromDb() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );

        ProductEntity product = ProductEntity.builder()
                .name("Star Helmet")
                .price(10.5)
                .description("Cool star helmet for space cats")
                .status(true)
                .category(cat)
                .build();

        ProductEntity saved = productDbService.saveProduct(product);

        var found = productDbService.findProductById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Star Helmet", found.get().getName());
    }

    @Test
    @DisplayName("shouldUpdateProductInDb: Update existing product")
    void shouldUpdateProductInDb() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder()
                        .name("Helmets")
                        .build()
        );

        ProductEntity product = ProductEntity.builder()
                .name("Star Helmet")
                .description("Cool helmet for space cats")
                .price(10.5)
                .status(true)
                .category(cat)
                .build();

        ProductEntity saved = productDbService.saveProduct(product);

        saved.setName("Star Helmet cooler");
        saved.setDescription("Updated description");
        saved.setPrice(20.0);

        ProductEntity updated = productDbService.saveProduct(saved);

        Optional<ProductEntity> foundOpt = productDbService.findProductById(updated.getId());
        assertTrue(foundOpt.isPresent());
        ProductEntity found = foundOpt.get();
        assertEquals("Updated description", found.getDescription());
        assertEquals(20.0, found.getPrice());
    }

    @Test
    @DisplayName("shouldDeleteProductFromDb: Delete existing product")
    void shouldDeleteProductFromDb() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder()
                        .name("Helmets")
                        .build()
        );

        ProductEntity product = ProductEntity.builder()
                .name("Star Helmet to delete")
                .description("To be deleted")
                .price(9.99)
                .status(true)
                .category(cat)
                .build();

        ProductEntity saved = productDbService.saveProduct(product);
        Long id = saved.getId();

        productDbService.deleteProduct(id);
        assertTrue(productDbService.findProductById(id).isEmpty());
    }
}
