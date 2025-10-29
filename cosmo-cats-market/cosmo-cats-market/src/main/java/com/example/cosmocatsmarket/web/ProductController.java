//package com.example.cosmocatsmarket.web;
//
//import com.example.cosmocatsmarket.dto.ProductDto;
//import com.example.cosmocatsmarket.service.ProductService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.UUID;
//
//@Slf4j
//@RestController
//@Validated
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/products")
//public class ProductController {
//    private final ProductService productService;
//
//    @GetMapping
//    public ResponseEntity<?> getAllProducts() {
//        log.debug("GET /api/v1/products - Fetching all products");
//        return ResponseEntity.ok(productService.getAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
//        log.debug("GET /api/v1/products/{} - Fetching product by ID", id);
//        return ResponseEntity.ok(productService.getById(id));
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
//        log.info("POST /api/v1/products - Creating product: {}", productDto.getName());
//        ProductDto created = productService.create(productDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ProductDto> updateProduct(
//            @PathVariable UUID id,
//            @Valid @RequestBody ProductDto productDto) {
//        log.info("PUT /api/v1/products/{} - Updating product", id);
//        ProductDto updated = productService.update(id, productDto);
//        return ResponseEntity.ok(updated);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id) {
//        log.warn("DELETE /api/v1/products/{} - Deleting product", id);
//        productService.deleteProductById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//}


