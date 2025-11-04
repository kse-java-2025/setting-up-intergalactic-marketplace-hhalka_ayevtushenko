package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import com.example.cosmocatsmarket.mapper.ProductMapper;
import com.example.cosmocatsmarket.service.ProductService;
import com.example.cosmocatsmarket.web.exception.ConflictException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final Map<UUID, Product> products = new HashMap<>();
    private final ProductMapper mapper;
    private final RestTemplate restTemplate = new RestTemplate();

    public ProductServiceImpl(ProductMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<ProductDTO> getAll() {
        return products.values().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public Optional<ProductDTO> getById(UUID id) {
        return Optional.ofNullable(products.get(id))
                .map(mapper::toDto);
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        boolean exists = products.values().stream()
                .anyMatch(p -> p.getProductName().equalsIgnoreCase(dto.getProductName()));

        if (exists) {
            throw new ConflictException("Product with the same name has already exists");
        }

        Product product = mapper.fromDTO(dto);
        product.setProductId(UUID.randomUUID());

        String url = "http://localhost:8090/prices/" + product.getProductId();
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("price")) {
                product.setPrice(Double.valueOf(response.get("price").toString()));
                System.out.println("Price updated from mock: " + product.getPrice());
            }
        } catch (Exception e) {
            System.out.println("Price service unavailable: " + e.getMessage());
        }

        products.put(product.getProductId(), product);
        return mapper.toDto(product);
    }

    @Override
    public Optional<ProductDTO> update(UUID id, ProductDTO dto) {
        if (!products.containsKey(id)) {
            return Optional.empty();
        }
        Product updated = mapper.fromDTO(dto);
        updated.setProductId(id);
        products.put(id, updated);
        return Optional.of(mapper.toDto(updated));
    }

    @Override
    public boolean delete(UUID id) {
        if (products == null) {
            return false;
        }
        products.remove(id);
        return true;
    }
}
