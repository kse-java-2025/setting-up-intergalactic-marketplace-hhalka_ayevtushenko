package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.repository.ProductRepository;
import com.example.cosmocatsmarket.repository.entity.ProductEntity;
import java.util.List;
import java.util.Optional;

public interface ProductDbService {
    ProductEntity saveProduct(ProductEntity product);
    Optional<ProductEntity> findProductById(Long id);
    Optional<ProductEntity> findProductByName(String name);
    List<ProductEntity> findAllProducts();
    List<ProductEntity> findProductsByStatus(Boolean status);
    List<ProductEntity> findProductsByPriceRange(Double minPrice, Double maxPrice);
    List<ProductEntity> searchProductsByName(String name);
    void deleteProduct(Long id);
    ProductEntity updateProduct(Long id, ProductEntity product);


    void deleteProductById(Long id);
}