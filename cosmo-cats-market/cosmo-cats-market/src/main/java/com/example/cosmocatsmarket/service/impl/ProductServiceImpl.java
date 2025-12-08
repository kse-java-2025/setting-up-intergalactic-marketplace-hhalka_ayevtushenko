package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.dto.ProductDTO;
import com.example.cosmocatsmarket.mapper.ProductMapper;
import com.example.cosmocatsmarket.repository.ProductRepository;
import com.example.cosmocatsmarket.repository.entity.ProductEntity;
import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import com.example.cosmocatsmarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDTO> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> getById(UUID id) {
        return productRepository.findById(convertUuidToLong(id))
                .map(this::convertToDto);
    }

    @Override
    public Optional<ProductDTO> getByName(String name) {
        return productRepository.findByName(name)
                .map(this::convertToDto);
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        if (!dto.getCategoryIds().isEmpty()) {
            Long firstCategoryId = convertStringToLong(dto.getCategoryIds().get(0));
            if (productRepository.existsByNameAndCategoryId(dto.getProductName(), firstCategoryId)) {
                throw new RuntimeException("Product with name '" + dto.getProductName() +
                        "' already exists in this category");
            }
        }

        ProductEntity entity = convertToEntity(dto);
        ProductEntity saved = productRepository.save(entity);
        return convertToDto(saved);
    }

    @Override
    public Optional<ProductDTO> update(UUID id, ProductDTO dto) {
        Long entityId = convertUuidToLong(id);
        return productRepository.findById(entityId)
                .map(existing -> {
                    if (!existing.getName().equals(dto.getProductName()) &&
                            !dto.getCategoryIds().isEmpty()) {
                        Long firstCategoryId = convertStringToLong(dto.getCategoryIds().get(0));
                        if (productRepository.existsByNameAndCategoryId(dto.getProductName(), firstCategoryId)) {
                            throw new RuntimeException("Product with name '" + dto.getProductName() +
                                    "' already exists in this category");
                        }
                    }

                    ProductEntity updated = convertToEntity(dto);
                    updated.setId(entityId);
                    ProductEntity saved = productRepository.save(updated);
                    return convertToDto(saved);
                });
    }

    @Override
    public boolean delete(UUID id) {
        Long entityId = convertUuidToLong(id);
        if (productRepository.existsById(entityId)) {
            productRepository.deleteById(entityId);
            return true;
        }
        return false;
    }


    @Override
    public List<ProductDTO> getByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getByStatus(Boolean status) {
        return productRepository.findByStatus(status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public boolean updateProductPrice(UUID id, Double newPrice) {
        Long entityId = convertUuidToLong(id);
        Optional<ProductEntity> productOpt = productRepository.findById(entityId);

        if (productOpt.isPresent()) {
            ProductEntity product = productOpt.get();

            if (newPrice <= 0) {
                throw new RuntimeException("Price must be greater than 0");
            }

            if (product.getPrice() != null && newPrice > product.getPrice() * 1.5) {
                throw new RuntimeException("Price increase cannot exceed 50%");
            }

            product.setPrice(newPrice);
            productRepository.save(product);
            return true;
        }

        return false;
    }

    @Override
    public ProductDTO cloneProduct(UUID id, String newName) {
        Long entityId = convertUuidToLong(id);
        ProductEntity original = productRepository.findById(entityId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (productRepository.existsByNameAndCategoryId(newName, original.getCategory().getId())) {
            throw new RuntimeException("Product with name '" + newName + "' already exists in this category");
        }

        ProductEntity clone = ProductEntity.builder()
                .name(newName)
                .description(original.getDescription())
                .price(original.getPrice())
                .status(original.getStatus())
                .category(original.getCategory())
                .build();

        ProductEntity savedClone = productRepository.save(clone);
        return convertToDto(savedClone);
    }



    private ProductDTO convertToDto(ProductEntity entity) {
        return ProductDTO.builder()
                .productId(convertLongToUuid(entity.getId()))
                .productName(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .status(entity.getStatus() != null ? entity.getStatus().toString() : "false")
                .categoryIds(entity.getCategory() != null ?
                        List.of(entity.getCategory().getId().toString()) : List.of())
                .build();
    }

    private ProductEntity convertToEntity(ProductDTO dto) {
        CategoryEntity category = null;
        if (!dto.getCategoryIds().isEmpty()) {
            Long categoryId = convertStringToLong(dto.getCategoryIds().get(0));
            category = CategoryEntity.builder()
                    .id(categoryId)
                    .build();
        }
        Boolean status = dto.getStatus() != null ?
                Boolean.parseBoolean(dto.getStatus()) : false;

        return ProductEntity.builder()
                .name(dto.getProductName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .status(status)
                .category(category)
                .build();
    }


    private Long convertUuidToLong(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return Math.abs(uuid.getMostSignificantBits() % Long.MAX_VALUE);
    }


    private UUID convertLongToUuid(Long id) {
        if (id == null) {
            return null;
        }
        return new UUID(id, 0L);
    }


    private Long convertStringToLong(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}