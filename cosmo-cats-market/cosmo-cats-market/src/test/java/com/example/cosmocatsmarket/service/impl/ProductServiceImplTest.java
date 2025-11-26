package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import com.example.cosmocatsmarket.mapper.ProductMapper;
import com.example.cosmocatsmarket.web.exception.ProductAlreadyExistsException;
import com.example.cosmocatsmarket.web.exception.ProductNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {


    @Mock
    private ProductMapper mapper;

    @Mock
    private PriceClient priceClient;

    @Mock
    private org.springframework.web.client.RestClient restClient;

    @InjectMocks
    private ProductServiceImpl service;
    private ProductDTO dto;
    private UUID id;
    private Product product;


    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        dto = new ProductDTO();
        dto.setProductName("Star Helmet");
        dto.setPrice(400.0);
        product = Product.builder()
                .productId(id)
                .productName("Star Helmet")
                .description(null)
                .price(400.0)
                .review(null)
                .status(null)
                .categories(List.of())
                .categoryIds(List.of())
                .build();
    }


    @Test
    void shouldReturnAllProducts() {
        ProductDTO mappedDto = new ProductDTO();
        mappedDto.setProductId(id);
        mappedDto.setProductName("Star Helmet");
        mappedDto.setPrice(400.0);

        when(mapper.toNewProduct(any(ProductDTO.class), any(UUID.class)))
                .thenReturn(product);

        when(priceClient.getPrice(any(UUID.class)))
                .thenReturn(400.0);

        when(mapper.toDto(any(Product.class)))
                .thenReturn(mappedDto);

        service.createProduct(dto);
        List<ProductDTO> result = service.getAllProducts();
        assertEquals(1, result.size());
        assertEquals("Star Helmet", result.get(0).getProductName());
    }


    @Test
    void shouldReturnEmptyListWhenNoProductsExist(){
        List<ProductDTO> result = service.getAllProducts();
        assertTrue(result.isEmpty());
    }


    @Test
    void shouldThrowConflictExceptionIfProductAlreadyExist() {
        when(mapper.toNewProduct(any(ProductDTO.class), any(UUID.class)))
                .thenReturn(product);

        when(priceClient.getPrice(any(UUID.class)))
                .thenReturn(400.0);

        when(mapper.toDto(any(Product.class)))
                .thenReturn(dto);

        service.createProduct(dto);
        assertThrows(ProductAlreadyExistsException.class,
                () -> service.createProduct(dto));
    }


    @Test
    void shouldReturnProductIfIdExist() {
        ProductDTO createDto = new ProductDTO();
        createDto.setProductName("Comet Helmet");
        createDto.setPrice(400.0);

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        when(mapper.toNewProduct(any(ProductDTO.class), idCaptor.capture()))
                .thenAnswer(invocation -> {
                    UUID realId = idCaptor.getValue();
                    return Product.builder()
                            .productId(realId)
                            .productName("Comet Helmet")
                            .price(400.0)
                            .categoryIds(List.of())
                            .categories(List.of())
                            .build();
                });

        when(priceClient.getPrice(any(UUID.class))).thenReturn(400.0);

        when(mapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO dto = new ProductDTO();
            dto.setProductId(p.getProductId());
            dto.setProductName(p.getProductName());
            dto.setPrice(p.getPrice());
            return dto;
        });

        ProductDTO created = service.createProduct(createDto);
        ProductDTO found = service.getProductById(created.getProductId());
        assertNotNull(found);
        assertEquals("Comet Helmet", found.getProductName());
        assertEquals(400.0, found.getPrice());
    }


    @Test
    void shouldThrowNotFoundWhenIdIsNull() {
        assertThrows(ProductNotFoundException.class,
                () -> service.getProductById(null));
    }


    @Test
    void shouldThrowWhenIdIsNull() {
        assertThrows(ProductNotFoundException.class,
                () -> service.getProductById(null));
    }

    @Test
    void shouldCreateProductCorrect() {
        when(mapper.toNewProduct(any(ProductDTO.class), any(UUID.class)))
                .thenReturn(product);

        when(priceClient.getPrice(any(UUID.class)))
                .thenReturn(product.getPrice());

        when(mapper.toDto(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product p = invocation.getArgument(0);
                    ProductDTO result = new ProductDTO();
                    result.setProductId(p.getProductId());
                    result.setProductName(p.getProductName());
                    result.setPrice(p.getPrice());
                    return result;
                });

        ProductDTO created = service.createProduct(dto);
        assertNotNull(created.getProductId());
        assertEquals(dto.getProductName(), created.getProductName());
        assertEquals(dto.getPrice(), created.getPrice());
    }


    @Test
    void shouldThrowConflictWhenProductAlreadyExists() {
        UUID id = UUID.randomUUID();
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Star Helmet");
        dto.setPrice(100.0);

        Product product = Product.builder()
                .productId(id)
                .productName("Star Helmet")
                .description(null)
                .price(100.0)
                .review(null)
                .status(null)
                .categories(List.of())
                .categoryIds(List.of())
                .build();


        when(mapper.toNewProduct(any(ProductDTO.class), any(UUID.class)))
                .thenReturn(product);

        when(priceClient.getPrice(any(UUID.class)))
                .thenReturn(100.0);

        when(mapper.toDto(any(Product.class)))
                .thenReturn(dto);

        service.createProduct(dto);
        assertThrows(ProductAlreadyExistsException.class,
                () -> service.createProduct(dto));
    }


    @Test
    void shouldStillCreateProductWhenPriceServiceUnavailable() {
        UUID id = UUID.randomUUID();
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Comet Shoes");
        dto.setPrice(150.0);

        Product product = Product.builder()
                .productId(id)
                .productName("Comet Shoes")
                .description(null)
                .price(150.0)
                .review(null)
                .status(null)
                .categories(List.of())
                .categoryIds(List.of())
                .build();

        ProductDTO mappedDto = new ProductDTO();
        mappedDto.setProductId(id);
        mappedDto.setProductName("Comet Shoes");
        mappedDto.setPrice(150.0);
        when(mapper.toNewProduct(any(ProductDTO.class), any(UUID.class)))
                .thenReturn(product);

        when(priceClient.getPrice(any(UUID.class)))
                .thenThrow(new RuntimeException("Service down"));

        when(mapper.toDto(any(Product.class)))
                .thenReturn(mappedDto);
        ProductDTO created = service.createProduct(dto);

        assertNotNull(created);
        assertEquals("Comet Shoes", created.getProductName());
        assertEquals(150.0, created.getPrice());
    }


    @Test
    void shouldUpdateProductWhenIdExists() {

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        when(mapper.toNewProduct(any(), idCaptor.capture()))
                .thenAnswer(invocation -> {
                    UUID generated = idCaptor.getValue();
                    return Product.builder()
                            .productId(generated)
                            .productName(dto.getProductName())
                            .price(dto.getPrice())
                            .categories(List.of())
                            .categoryIds(List.of())
                            .build();
                });

        when(priceClient.getPrice(any()))
                .thenReturn(dto.getPrice());

        when(mapper.toDto(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO mapped = new ProductDTO();
            mapped.setProductId(p.getProductId());
            mapped.setProductName(p.getProductName());
            mapped.setPrice(p.getPrice());
            return mapped;
        });

        ProductDTO created = service.createProduct(dto);
        UUID actualId = created.getProductId();
        ProductDTO updateDto = new ProductDTO();
        updateDto.setProductName("Updated Helmet");
        updateDto.setPrice(350.0);

        when(mapper.toUpdatedProduct(updateDto, actualId))
                .thenReturn(
                        Product.builder()
                                .productId(actualId)
                                .productName("Updated Helmet")
                                .price(350.0)
                                .categories(List.of())
                                .categoryIds(List.of())
                                .build()
                );

        ProductDTO result = service.updateProduct(actualId, updateDto);
        assertNotNull(result);
        assertEquals("Updated Helmet", result.getProductName());
        assertEquals(350.0, result.getPrice());
    }


    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistingProduct() {
        UUID fakeId = UUID.randomUUID();
        ProductDTO dto = new ProductDTO();

        dto.setProductName("Ghost Product");
        dto.setPrice(999.0);

        assertThrows(ProductNotFoundException.class,
                () -> service.updateProduct(fakeId, dto));
    }


    @Test
    void shouldDeleteProductWhenIdExists() {
        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        when(mapper.toNewProduct(any(), idCaptor.capture()))
                .thenAnswer(invocation -> {
                    UUID generated = idCaptor.getValue();
                    return Product.builder()
                            .productId(generated)
                            .productName(product.getProductName())
                            .price(product.getPrice())
                            .categories(List.of())
                            .categoryIds(List.of())
                            .build();
                });

        when(priceClient.getPrice(any()))
                .thenReturn(product.getPrice());

        when(mapper.toDto(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO d = new ProductDTO();
            d.setProductId(p.getProductId());
            d.setProductName(p.getProductName());
            d.setPrice(p.getPrice());
            return d;
        });

        ProductDTO created = service.createProduct(dto);
        UUID actualId = created.getProductId();

        assertNotNull(actualId);
        assertDoesNotThrow(() -> service.deleteProduct(actualId));
        assertTrue(service.getAllProducts().isEmpty());
    }


    @Test
    void shouldThrowExceptionWhenDeletingWithNullMap() throws Exception {
        var field = ProductServiceImpl.class.getDeclaredField("products");
        field.setAccessible(true);
        field.set(service, null);
        assertThrows(NullPointerException.class,
                () -> service.deleteProduct(UUID.randomUUID()));
    }


    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID missingId = UUID.randomUUID();

        assertThrows(ProductNotFoundException.class,
                () -> service.getProductById(missingId));
    }


    @Test
    void shouldThrowExceptionWhenDeletingMissingProduct() {
        UUID missingId = UUID.randomUUID();

        assertThrows(ProductNotFoundException.class,
                () -> service.deleteProduct(missingId));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingProduct() {
        UUID missing = UUID.randomUUID();

        ProductDTO update = new ProductDTO();
        update.setProductName("Missing");
        update.setPrice(10.0);

        assertThrows(ProductNotFoundException.class,
                () -> service.updateProduct(missing, update));
    }


    @Test
    void shouldReturnAllProductsAfterMultipleCreates() {
        when(mapper.toNewProduct(any(), any()))
                .thenAnswer(invocation -> {
                    ProductDTO in = invocation.getArgument(0);
                    UUID id = invocation.getArgument(1);
                    return Product.builder()
                            .productId(id)
                            .productName(in.getProductName())
                            .price(in.getPrice())
                            .categories(List.of())
                            .categoryIds(List.of())
                            .build();
                });

        when(priceClient.getPrice(any())).thenReturn(100.0);
        when(mapper.toDto(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO dto = new ProductDTO();
            dto.setProductId(p.getProductId());
            dto.setProductName(p.getProductName());
            dto.setPrice(p.getPrice());
            return dto;
        });

        ProductDTO dto1 = new ProductDTO();
        dto1.setProductName("A");
        dto1.setPrice(100.0);

        ProductDTO dto2 = new ProductDTO();
        dto2.setProductName("B");
        dto2.setPrice(100.0);

        service.createProduct(dto1);
        service.createProduct(dto2);

        List<ProductDTO> all = service.getAllProducts();

        assertEquals(2, all.size());
    }



    @Test
    void shouldThrowWhenCreatingProductWithNullName() {
        ProductDTO invalid = new ProductDTO();
        invalid.setProductName(null);
        invalid.setPrice(100.0);

        assertThrows(NullPointerException.class,
                () -> service.createProduct(invalid));
    }


    @Test
    void shouldReturnEmptyListAfterDeleteAll() {
        when(mapper.toNewProduct(any(), any(UUID.class)))
                .thenAnswer(invocation -> {
                    ProductDTO in = invocation.getArgument(0);
                    UUID id = invocation.getArgument(1);

                    return Product.builder()
                            .productId(id)
                            .productName(in.getProductName())
                            .price(in.getPrice())
                            .categories(List.of())
                            .categoryIds(List.of())
                            .build();
                });

        when(priceClient.getPrice(any())).thenReturn(product.getPrice());

        when(mapper.toDto(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO d = new ProductDTO();
            d.setProductId(p.getProductId());
            d.setProductName(p.getProductName());
            d.setPrice(p.getPrice());
            return d;
        });

        ProductDTO created = service.createProduct(dto);
        UUID id = created.getProductId();
        service.deleteProduct(id);
        assertTrue(service.getAllProducts().isEmpty());
    }
}