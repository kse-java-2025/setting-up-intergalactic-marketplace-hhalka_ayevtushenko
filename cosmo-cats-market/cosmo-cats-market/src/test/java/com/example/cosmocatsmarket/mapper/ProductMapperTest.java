package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class ProductMapperTest {
    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    @DisplayName("shouldMapProductToDto: Map product to DTO ")
    void shouldMapProductToDto() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setProductId(id);
        product.setProductName("Space Snack");
        product.setDescription("Tasty snack");
        product.setPrice(10.5);
        product.setReview("Very tasty");
        product.setStatus("Available");

        ProductDTO dto = mapper.toDto(product);

        assertNotNull(dto);
        assertEquals(product.getProductId(), dto.getProductId());
        assertEquals(product.getProductName(), dto.getProductName());
        assertEquals(product.getDescription(), dto.getDescription());
        assertEquals(product.getPrice(), dto.getPrice());
        assertEquals(product.getReview(), dto.getReview());
        assertEquals(product.getStatus(), dto.getStatus());
    }

    @Test
    @DisplayName("shouldMapDtoToProductIgnoringId: Map DTO to product")
    void shouldMapDtoToProductIgnoringId() {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(UUID.randomUUID());
        dto.setProductName("Space Snack");
        dto.setDescription("Tasty snack");
        dto.setPrice(10.5);
        dto.setReview("Very tasty");
        dto.setStatus("Available");

        Product product = mapper.fromDTO(dto);

        assertNotNull(product);
        assertNull(product.getProductId(), "productId must be null");
        assertEquals(dto.getProductName(), product.getProductName());
        assertEquals(dto.getDescription(), product.getDescription());
        assertEquals(dto.getPrice(), product.getPrice());
        assertEquals(dto.getReview(), product.getReview());
        assertEquals(dto.getStatus(), product.getStatus());
    }
}
