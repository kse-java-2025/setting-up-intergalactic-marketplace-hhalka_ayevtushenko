package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.dto.ProductDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(UUID id);
    ProductDTO createProduct(ProductDTO dto);
    ProductDTO updateProduct(UUID id, ProductDTO dto);
    void deleteProduct(UUID id);
}