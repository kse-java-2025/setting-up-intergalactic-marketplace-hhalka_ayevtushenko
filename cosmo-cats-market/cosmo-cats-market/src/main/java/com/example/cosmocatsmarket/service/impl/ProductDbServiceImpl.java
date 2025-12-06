package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.repository.ProductRepository;
import com.example.cosmocatsmarket.repository.entity.ProductEntity;
import com.example.cosmocatsmarket.service.ProductDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductDbServiceImpl implements ProductDbService {
    private final ProductRepository productRepository;

    @Override
    public ProductEntity saveProduct(ProductEntity product) {
        if (productRepository.existsByNameAndCategoryId(product.getName(), product.getCategory().getId())) {
            throw new RuntimeException("Product with name '" + product.getName() +
                    "' already exists in this category");
        }
        return productRepository.save(product);
    }

    @Override
    public Optional<ProductEntity> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<ProductEntity> findProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<ProductEntity> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductEntity> findProductsByStatus(Boolean status) {
        return productRepository.findByStatus(status);
    }

    @Override
    public List<ProductEntity> findProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<ProductEntity> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductEntity updateProduct(Long id, ProductEntity product) {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (!existingProduct.getName().equals(product.getName()) &&
                productRepository.existsByNameAndCategoryId(product.getName(), product.getCategory().getId())) {
            throw new RuntimeException("Product with name '" + product.getName() +
                    "' already exists in this category");
        }
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStatus(product.getStatus());
        existingProduct.setCategory(product.getCategory());

        return productRepository.save(existingProduct);
    }


    public ProductEntity cloneProduct(Long productId, String newName) {
        ProductEntity original = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductEntity clone = ProductEntity.builder()
                .name(newName)
                .description(original.getDescription())
                .price(original.getPrice())
                .status(original.getStatus())
                .category(original.getCategory())
                .build();
        if (productRepository.existsByNameAndCategoryId(newName, original.getCategory().getId())) {
            throw new RuntimeException("Product with name '" + newName + "' already exists in this category");
        }
        ProductEntity savedClone = productRepository.save(clone);
        return savedClone;
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}