package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import com.example.cosmocatsmarket.mapper.ProductMapper;
import com.example.cosmocatsmarket.repository.ProductRepository;
import com.example.cosmocatsmarket.web.exception.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

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
        ProductDTO mappedDto = new ProductDTO();
        mappedDto.setProductName("Star Helmet");
        mappedDto.setPrice(400.0);
        //invocation - object that consist info about call
        when(mapper.fromDTO(any(ProductDTO.class))).thenAnswer(invocation -> {
            Product p = new Product();
            p.setProductName(invocation.getArgument(0, ProductDTO.class).getProductName());
            return p;
        });

        when(mapper.toDto(any(Product.class))).thenReturn(mappedDto);
        service.create(mappedDto);
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
        when(mapper.fromDTO(any(ProductDTO.class))).thenAnswer(invocation ->{
            Product p = new Product();
            p.setProductName(invocation.getArgument(0, ProductDTO.class).getProductName());
            return  p;
        });
        when(mapper.toDto(any(Product.class))).thenReturn(dto);
        service.create(dto);
        assertThrows(ConflictException.class, () -> service.create(dto));
    }



    //------------


    @Test
    void shouldReturnProductIfIdExist() {
        ProductDTO mappedDto = new ProductDTO();
        mappedDto.setProductId(UUID.randomUUID());
        mappedDto.setProductName("Comet Helmet");
        mappedDto.setPrice(400.0);

        when(mapper.fromDTO(any(ProductDTO.class))).thenAnswer(invocation -> {
            Product p = new Product();
            p.setProductId(invocation.getArgument(0, ProductDTO.class).getProductId());
            p.setProductName(invocation.getArgument(0, ProductDTO.class).getProductName());
            p.setPrice(invocation.getArgument(0, ProductDTO.class).getPrice());
            return p;
        });

        when(mapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0, Product.class);
            ProductDTO dto = new ProductDTO();
            dto.setProductId(p.getProductId());
            dto.setProductName(p.getProductName());
            dto.setPrice(p.getPrice());
            return dto;
        });

        ProductDTO created = service.create(mappedDto);
        Optional<ProductDTO> result = service.getById(created.getProductId());

        assertTrue(result.isPresent());
        assertEquals("Comet Helmet", result.get().getProductName());
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


    //------------

    @Test
    void shouldCreateProductCorrect(){
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Galaxy Bowl");
        dto.setPrice(200.0);

        Product mapped = new Product();
        mapped.setProductName(dto.getProductName());
        mapped.setPrice(dto.getPrice());

        when(mapper.fromDTO(dto)).thenReturn(mapped);
        when(mapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO res = new ProductDTO();
            res.setProductId(p.getProductId());
            res.setProductName(p.getProductName());
            res.setPrice(p.getPrice());
            return res;
        });

        ProductDTO created = service.create(dto);
        assertNotNull(created.getProductId());
        assertEquals("Galaxy Bowl", created.getProductName());
    }


    @Test
    void shouldThrowConflictWhenProductAlreadyExists() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Star Helmet");

        Product p = new Product();
        p.setProductName("Star Helmet");

        when(mapper.fromDTO(dto)).thenReturn(p);
        when(mapper.toDto(any(Product.class))).thenReturn(dto);

        service.create(dto);
        assertThrows(ConflictException.class, () -> service.create(dto));
    }


    @Test
    void shouldStillCreateProductWhenPriceServiceUnavailable() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Comet Shoes");
        dto.setPrice(150.0);

        Product mapped = new Product();
        mapped.setProductName(dto.getProductName());
        mapped.setPrice(dto.getPrice());

        when(mapper.fromDTO(dto)).thenReturn(mapped);
        when(mapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO res = new ProductDTO();
            res.setProductId(p.getProductId());
            res.setProductName(p.getProductName());
            res.setPrice(p.getPrice());
            return res;
        });

        ProductDTO created = service.create(dto);
        assertNotNull(created);
        assertEquals("Comet Shoes", created.getProductName());
    }

    //------------

    @Test
    void shouldUpdateProductWhenIdExists() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Old Helmet");
        dto.setPrice(300.0);

        Product mapped = new Product();
        mapped.setProductName("Old Helmet");
        mapped.setPrice(300.0);

        when(mapper.fromDTO(any(ProductDTO.class))).thenReturn(mapped);
        when(mapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO d = new ProductDTO();
            d.setProductId(p.getProductId());
            d.setProductName(p.getProductName());
            d.setPrice(p.getPrice());
            return d;
        });


        ProductDTO created = service.create(dto);


        ProductDTO newDto = new ProductDTO();
        newDto.setProductName("Updated Helmet");
        newDto.setPrice(350.0);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Updated Helmet");
        updatedProduct.setPrice(350.0);
        when(mapper.fromDTO(newDto)).thenReturn(updatedProduct);

        Optional<ProductDTO> result = service.update(created.getProductId(), newDto);


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

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty()); // added
        Optional<ProductDTO> result = service.update(fakeId, dto);
        assertTrue(result.isEmpty());
    }


    //----------------

    @Test
    void shouldDeleteProductWhenIdExists() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Cosmo Mug");
        dto.setPrice(99.0);

        Product mapped = new Product();
        mapped.setProductName(dto.getProductName());
        mapped.setPrice(dto.getPrice());
        when(mapper.fromDTO(dto)).thenReturn(mapped);
        when(mapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO result = new ProductDTO();
            result.setProductId(p.getProductId());
            result.setProductName(p.getProductName());
            result.setPrice(p.getPrice());
            return result;
        });


        ProductDTO created = service.create(dto);

        boolean deleted = service.delete(created.getProductId());
        assertTrue(deleted, "Expected delete() to return true");
        assertTrue(service.getAll().isEmpty(), "Expected no products after deletion");
    }


    @Test
    void shouldReturnFalseWhenProductsMapIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        var field = ProductServiceImpl.class.getDeclaredField("products");
        field.setAccessible(true);
        field.set(service, null);

        boolean result = service.delete(id);
        assertFalse(result, "Expected delete() to return false when map is null");
    }
}
