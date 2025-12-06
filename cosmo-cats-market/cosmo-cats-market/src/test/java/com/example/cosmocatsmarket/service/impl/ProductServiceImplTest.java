package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import com.example.cosmocatsmarket.mapper.ProductMapper;
import com.example.cosmocatsmarket.repository.ProductRepository;
import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import com.example.cosmocatsmarket.repository.entity.ProductEntity;
import com.example.cosmocatsmarket.web.exception.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductMapper mapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl service;

    private Product product;
    private ProductDTO dto;

    @BeforeEach
    void setup(){
        product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("Star Helmet");
        product.setPrice(400.00);

        dto = new ProductDTO();
        dto.setProductName("Star Helmet");
        dto.setPrice(400.00);
    }


    @Test
    void shouldReturnAllProducts() {
        ProductDTO input = new ProductDTO();
        input.setProductName("Star Helmet");
        input.setPrice(400.0);
        input.setCategoryIds(List.of());
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(inv -> {
            ProductEntity e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        when(productRepository.findAll()).thenReturn(
                List.of(ProductEntity.builder()
                        .id(1L)
                        .name("Star Helmet")
                        .price(400.0)
                        .build())
        );

        service.create(input);
        List<ProductDTO> result = service.getAll();
        assertEquals(1, result.size());
        assertEquals("Star Helmet", result.get(0).getProductName());
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsExist(){
        List<ProductDTO> result = service.getAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowConflictExceptionIfProductAlreadyExist() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Star Helmet");
        dto.setCategoryIds(List.of("1"));

        when(productRepository.existsByNameAndCategoryId("Star Helmet", 1L))
                .thenReturn(false)
                .thenReturn(true);

        when(productRepository.save(any())).thenAnswer(inv -> {
            ProductEntity e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        service.create(dto);
        assertThrows(RuntimeException.class, () -> service.create(dto));
    }

    @Test
    void shouldReturnProductIfIdExists() {
        UUID id = UUID.randomUUID();
        Long longId = Math.abs(id.getMostSignificantBits() % Long.MAX_VALUE);
        ProductEntity entity = ProductEntity.builder()
                .id(longId)
                .name("Comet Helmet")
                .description("Test desc")
                .price(400.0)
                .status(true)
                .category(null)
                .build();

        when(productRepository.findById(longId)).thenReturn(Optional.of(entity));
        Optional<ProductDTO> result = service.getById(id);
        assertTrue(result.isPresent());
        assertEquals("Comet Helmet", result.get().getProductName());
        assertEquals(400.0, result.get().getPrice());
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        UUID randomId = UUID.randomUUID();
        Optional<ProductDTO> result = service.getById(randomId);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenIdIsNull() {
        Optional<ProductDTO> result = service.getById(null);
        assertTrue(result.isEmpty());
    }


    @Test
    void shouldCreateProductCorrect() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Galaxy Bowl");
        dto.setPrice(200.0);
        dto.setCategoryIds(List.of("1"));

        ProductEntity entityToSave = ProductEntity.builder()
                .name("Galaxy Bowl")
                .price(200.0)
                .category(CategoryEntity.builder().id(1L).build())
                .build();

        ProductEntity savedEntity = ProductEntity.builder()
                .id(10L)
                .name("Galaxy Bowl")
                .price(200.0)
                .category(CategoryEntity.builder().id(1L).build())
                .build();

        when(productRepository.save(any(ProductEntity.class))).thenReturn(savedEntity);
        ProductDTO created = service.create(dto);
        assertNotNull(created);
        assertEquals("Galaxy Bowl", created.getProductName());
        assertEquals(200.0, created.getPrice());
        assertEquals(new UUID(10L, 0L), created.getProductId());
    }


    @Test
    void shouldThrowConflictWhenProductAlreadyExists() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Star Helmet");
        dto.setCategoryIds(List.of("5"));
        when(productRepository.existsByNameAndCategoryId("Star Helmet", 5L))
                .thenReturn(true);
        assertThrows(RuntimeException.class, () -> service.create(dto));
    }


    @Test
    void shouldStillCreateProductWhenPriceServiceUnavailable() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Comet Shoes");
        dto.setPrice(150.0);
        dto.setCategoryIds(List.of());

        ProductEntity savedEntity = ProductEntity.builder()
                .id(10L)
                .name("Comet Shoes")
                .price(150.0)
                .status(false)
                .build();

        when(productRepository.save(any(ProductEntity.class)))
                .thenReturn(savedEntity);

        ProductDTO created = service.create(dto);
        assertNotNull(created);
        assertEquals("Comet Shoes", created.getProductName());
        assertEquals(150.0, created.getPrice());
    }


    @Test
    void shouldUpdateProductWhenIdExists() {
        UUID id = UUID.randomUUID();
        Long idLong = Math.abs(id.getMostSignificantBits() % Long.MAX_VALUE);

        ProductEntity existing = ProductEntity.builder()
                .id(idLong)
                .name("Old Helmet")
                .price(300.0)
                .status(false)
                .build();

        ProductDTO dto = new ProductDTO();
        dto.setProductName("Updated Helmet");
        dto.setPrice(350.0);
        dto.setCategoryIds(List.of());

        ProductEntity updated = ProductEntity.builder()
                .id(idLong)
                .name("Updated Helmet")
                .price(350.0)
                .status(false)
                .build();

        when(productRepository.findById(idLong)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(updated);

        Optional<ProductDTO> result = service.update(id, dto);
        assertTrue(result.isPresent());
        assertEquals("Updated Helmet", result.get().getProductName());
        assertEquals(350.0, result.get().getPrice());
    }


    @Test
    void shouldReturnEmptyWhenUpdatingNonExistingProduct() {
        UUID fakeId = UUID.randomUUID();
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Ghost Product");
        dto.setPrice(999.0);

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<ProductDTO> result = service.update(fakeId, dto);
        assertTrue(result.isEmpty());
    }


    @Test
    void shouldDeleteProductWhenIdExists() {
        UUID id = UUID.randomUUID();
        Long idLong = Math.abs(id.getMostSignificantBits() % Long.MAX_VALUE);

        when(productRepository.existsById(idLong)).thenReturn(true);

        boolean result = service.delete(id);
        assertTrue(result);
        verify(productRepository).deleteById(idLong);
    }


    @Test
    void shouldReturnFalseWhenProductDoesNotExist() {
        UUID id = UUID.randomUUID();
        Long idLong = Math.abs(id.getMostSignificantBits() % Long.MAX_VALUE);

        when(productRepository.existsById(idLong)).thenReturn(false);
        boolean result = service.delete(id);
        assertFalse(result);
        verify(productRepository, never()).deleteById(any());
    }

}
