package com.example.cosmocatsmarket.web;

import com.example.cosmocatsmarket.dto.ProductDTO;
import com.example.cosmocatsmarket.repository.ProductRepository;
import com.example.cosmocatsmarket.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        log.debug("GET /api/v1/products - Fetching all products");
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        log.debug("GET /api/v1/products/{} - Fetching product by ID", id);
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable String name) {
        log.debug("GET /api/v1/products/name/{} - Fetching product by name", name);
        return productService.getByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        log.debug("GET /api/v1/products/category/{} - Fetching products by category", categoryId);
        return ResponseEntity.ok(productService.getByCategory(categoryId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductDTO>> getProductsByStatus(@PathVariable Boolean status) {
        log.debug("GET /api/v1/products/status/{} - Fetching products by status", status);
        return ResponseEntity.ok(productService.getByStatus(status));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductDTO>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        log.debug("GET /api/v1/products/price-range?minPrice={}&maxPrice={} - Fetching products by price range",
                minPrice, maxPrice);
        return ResponseEntity.ok(productService.getByPriceRange(minPrice, maxPrice));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String name) {
        log.debug("GET /api/v1/products/search?name={} - Searching products", name);
        return ResponseEntity.ok(productService.searchByName(name));
    }


    @GetMapping("/reports/category-stats")
    public ResponseEntity<List<ProductRepository.CategoryPriceStats>> getCategoryPriceStatistics() {
        log.debug("GET /api/v1/products/reports/category-stats - Getting category price statistics");
        List<ProductRepository.CategoryPriceStats> stats = productRepository.getCategoryPriceStatistics();
        return ResponseEntity.ok(stats);
    }


    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDto) {
        log.info("POST /api/v1/products - Creating product: {}", productDto.getProductName());
        ProductDTO created = productService.create(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDTO productDto) {
        log.info("PUT /api/v1/products/{} - Updating product", id);
        return productService.update(id, productDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}/price")
    public ResponseEntity<ProductDTO> updateProductPrice(
            @PathVariable UUID id,
            @RequestParam Double newPrice) {
        log.info("PUT /api/v1/products/{}/price?newPrice={} - Updating product price", id, newPrice);
        try {
            productService.updateProductPrice(id, newPrice);
            return productService.getById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            log.error("Error updating product price: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/{id}/clone")
    public ResponseEntity<ProductDTO> cloneProduct(
            @PathVariable UUID id,
            @RequestParam String newName) {
        log.info("POST /api/v1/products/{}/clone?newName={} - Cloning product", id, newName);
        try {
            ProductDTO cloned = productService.cloneProduct(id, newName);
            return ResponseEntity.ok(cloned);
        } catch (RuntimeException e) {
            log.error("Error cloning product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id) {
        log.warn("DELETE /api/v1/products/{} - Deleting product", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}