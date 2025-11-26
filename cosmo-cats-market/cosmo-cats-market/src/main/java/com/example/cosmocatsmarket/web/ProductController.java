package com.example.cosmocatsmarket.web;

import com.example.cosmocatsmarket.dto.ProductDTO;
import com.example.cosmocatsmarket.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        log.debug("GET /api/v1/products - Fetching all products");
        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable UUID id) {
        log.debug("GET /api/v1/products/{} - Fetching product by ID", id);
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDto) {
        log.info("POST /api/v1/products - Creating product: {}", productDto.getProductName());
        ProductDTO created = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDTO productDto
    ) {
        log.info("PUT /api/v1/products/{} - Updating product", id);
        return productService.updateProduct(id, productDto);
    }


    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id) {
        log.warn("DELETE /api/v1/products/{} - Deleting product", id);
        productService.deleteProduct(id);
    }

}
