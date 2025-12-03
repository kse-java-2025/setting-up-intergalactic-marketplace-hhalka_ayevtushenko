package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.dto.ProductDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    List<ProductDTO> getAll();
    Optional<ProductDTO> getById(UUID id);
    Optional<ProductDTO> getByName(String name);
    ProductDTO create(ProductDTO dto);
    Optional<ProductDTO> update(UUID id, ProductDTO dto);
    boolean delete(UUID id);

    List<ProductDTO> getByCategory(Long categoryId);
    List<ProductDTO> getByStatus(Boolean status);
    List<ProductDTO> getByPriceRange(Double minPrice, Double maxPrice);

    List<ProductDTO> searchByName(String name);
    boolean updateProductPrice(UUID id, Double newPrice);
    ProductDTO cloneProduct(UUID id, String newName);
}
