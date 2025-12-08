package com.example.cosmocatsmarket.IT;

import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.repository.ProductRepository;
import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import com.example.cosmocatsmarket.repository.entity.ProductEntity;
import com.example.cosmocatsmarket.service.ProductDbService;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void cleanDb() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }


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


    @Test
    @DisplayName("deleteProductById: should delete product by id")
    void shouldDeleteProductById() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );

        ProductEntity product = productRepository.save(
                ProductEntity.builder()
                        .name("DeleteMe")
                        .description("To be deleted")
                        .price(9.99)
                        .status(true)
                        .category(cat)
                        .build()
        );

        Long id = product.getId();
        productDbService.deleteProductById(id);
        assertTrue(productDbService.findProductById(id).isEmpty());
        assertFalse(productRepository.existsById(id));
    }


    @Test
    @DisplayName("getAll: should return all products from DB")
    void shouldReturnAllProducts() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );

        ProductEntity p1 = productRepository.save(ProductEntity.builder()
                .name("Helmet A")
                .description("desc A")
                .price(10.0)
                .status(true)
                .category(cat)
                .build()
        );

        ProductEntity p2 = productRepository.save(ProductEntity.builder()
                .name("Helmet B")
                .description("desc B")
                .price(20.0)
                .status(false)
                .category(cat)
                .build()
        );

        var all = productDbService.findAllProducts();
        assertEquals(2, all.size());
    }


    @Test
    @DisplayName("getById: should find product by id")
    void shouldFindProductById() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );

        ProductEntity saved = productRepository.save(ProductEntity.builder()
                .name("Test")
                .description("test description")
                .price(5.0)
                .status(true)
                .category(cat)
                .build()
        );

        Optional<ProductEntity> found = productDbService.findProductById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getName());
    }


    @Test
    @DisplayName("getByName: should return product by name")
    void shouldReturnProductByName() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );
        productRepository.save(ProductEntity.builder()
                .name("UniqueName123")
                .description("desc")
                .price(5.0)
                .status(true)
                .category(cat)
                .build()
        );
        Optional<ProductEntity> found = productDbService.findProductByName("UniqueName123");
        assertTrue(found.isPresent());
        assertEquals("UniqueName123", found.get().getName());
    }


    @Test
    @DisplayName("create: should save new product")
    void shouldCreateProduct() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );

        ProductEntity product = ProductEntity.builder()
                .name("New Helmet")
                .description("desc")
                .price(22.0)
                .status(true)
                .category(cat)
                .build();

        ProductEntity saved = productDbService.saveProduct(product);
        assertNotNull(saved.getId());
        assertEquals("New Helmet", saved.getName());
    }


    @Test
    @DisplayName("create: should throw if product already exists")
    void shouldNotCreateIfExists() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );

        productRepository.save(ProductEntity.builder()
                .name("Duplicate")
                .description("desc")
                .price(15.0)
                .status(true)
                .category(cat)
                .build()
        );

        ProductEntity duplicate = ProductEntity.builder()
                .name("Duplicate")
                .description("desc")
                .price(15.0)
                .status(true)
                .category(cat)
                .build();
        assertThrows(RuntimeException.class, () -> productDbService.saveProduct(duplicate));
    }


    @Test
    @DisplayName("getByStatus: should filter by status")
    void shouldFilterByStatus() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );

        productRepository.save(ProductEntity.builder()
                .name("A")
                .description("d")
                .price(10.0)
                .status(true)
                .category(cat)
                .build()
        );

        productRepository.save(ProductEntity.builder()
                .name("B")
                .description("d")
                .price(15.0)
                .status(false)
                .category(cat)
                .build()
        );
        var active = productDbService.findProductsByStatus(true);
        assertEquals(1, active.size());
    }


    @Test
    @DisplayName("updateProduct: should update existing product fields")
    void shouldUpdateProductById() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder()
                        .name("Helmets")
                        .build()
        );

        ProductEntity original = productRepository.save(ProductEntity.builder()
                .name("Helmet A")
                .description("Old desc")
                .price(5.0)
                .status(true)
                .category(cat)
                .build()
        );

        ProductEntity updatedData = ProductEntity.builder()
                .name("Helmet A Updated")
                .description("New desc")
                .price(15.0)
                .status(false)
                .category(cat)
                .build();

        ProductEntity updated = productDbService.updateProduct(original.getId(), updatedData);
        assertEquals("Helmet A Updated", updated.getName());
        assertEquals("New desc", updated.getDescription());
        assertEquals(15.0, updated.getPrice());
        assertFalse(updated.getStatus());
    }


    @Test
    @DisplayName("updateProduct: should throw if new product name already exists in category")
    void shouldThrowWhenUpdatingToDuplicateName() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder()
                        .name("Helmets")
                        .build()
        );

        ProductEntity existing = productRepository.save(ProductEntity.builder()
                .name("Helmet Original")
                .description("desc")
                .price(10.0)
                .status(true)
                .category(cat)
                .build()
        );

        productRepository.save(ProductEntity.builder()
                .name("Helmet Conflict")
                .description("desc")
                .price(15.0)
                .status(true)
                .category(cat)
                .build()
        );

        ProductEntity updatedData = ProductEntity.builder()
                .name("Helmet Conflict")
                .description("new desc")
                .price(99.0)
                .status(false)
                .category(cat)
                .build();

        assertThrows(RuntimeException.class,
                () -> productDbService.updateProduct(existing.getId(), updatedData));
    }


    @Test
    @DisplayName("getByPriceRange: should return products in price range")
    void shouldFindProductsByPriceRange() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );

        productRepository.save(ProductEntity.builder()
                .name("Cheap Helmet")
                .description("desc")
                .price(5.0)
                .status(true)
                .category(cat)
                .build()
        );

        productRepository.save(ProductEntity.builder()
                .name("Mid Helmet")
                .description("desc")
                .price(15.0)
                .status(true)
                .category(cat)
                .build()
        );

        productRepository.save(ProductEntity.builder()
                .name("Expensive Helmet")
                .description("desc")
                .price(50.0)
                .status(true)
                .category(cat)
                .build()
        );

        var found = productDbService.findProductsByPriceRange(10.0, 20.0);
        assertEquals(1, found.size());
        assertEquals("Mid Helmet", found.get(0).getName());
    }



    @Test
    @DisplayName("searchByName: should return products containing search text")
    void shouldSearchProductsByName() {
        CategoryEntity cat = categoryRepository.save(
                CategoryEntity.builder().name("Helmets").build()
        );

        productRepository.save(ProductEntity.builder()
                .name("Super Helmet")
                .description("desc")
                .price(10.0)
                .status(true)
                .category(cat)
                .build()
        );

        productRepository.save(ProductEntity.builder()
                .name("Helmet Deluxe")
                .description("desc")
                .price(20.0)
                .status(true)
                .category(cat)
                .build()
        );

        productRepository.save(ProductEntity.builder()
                .name("Boots")
                .description("desc")
                .price(30.0)
                .status(true)
                .category(cat)
                .build()
        );

        var found = productDbService.searchProductsByName("helmet");
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(p -> p.getName().equals("Super Helmet")));
        assertTrue(found.stream().anyMatch(p -> p.getName().equals("Helmet Deluxe")));
    }

}
