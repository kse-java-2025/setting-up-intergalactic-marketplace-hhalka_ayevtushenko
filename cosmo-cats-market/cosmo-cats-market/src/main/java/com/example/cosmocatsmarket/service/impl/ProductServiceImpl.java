package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import com.example.cosmocatsmarket.mapper.ProductMapper;
import com.example.cosmocatsmarket.service.ProductService;
import com.example.cosmocatsmarket.web.exception.ProductAlreadyExistsException;
import com.example.cosmocatsmarket.web.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final Map<UUID, Product> products = new HashMap<>();
    private final ProductMapper mapper;
    private final RestClient restClient;
    private final PriceClient priceClient;

    @Override
    public List<ProductDTO> getAllProducts() {
        return products.values().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ProductDTO getProductById(UUID id) {
        if (id == null) {
            throw new ProductNotFoundException("null");
        }
        Product product = products.get(id);

        if (product == null) {
            throw new ProductNotFoundException(id.toString());
        }
        return mapper.toDto(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO dto) {

        boolean exists = products.values().stream()
                .anyMatch(p -> p.getProductName().equalsIgnoreCase(dto.getProductName()));

        if (exists) {
            throw new ProductAlreadyExistsException(dto.getProductName());
        }
        UUID id = UUID.randomUUID();
        Product product = mapper.toNewProduct(dto, id);
        Double price;

        try {
            price = priceClient.getPrice(id);
        }
        catch (Exception ex) {
            price = product.getPrice();
        }

        product = product.toBuilder()
                .price(price)
                .build();
        products.put(id, product);
        return mapper.toDto(product);
    }

    @Override
    public ProductDTO updateProduct(UUID id, ProductDTO dto) {
        Product existing = products.get(id);

        if (existing == null) {
            throw new ProductNotFoundException(id.toString());
        }
        Product updated = mapper.toUpdatedProduct(dto, id);
        products.put(updated.getProductId(), updated);
        return mapper.toDto(updated);
    }

    @Override
    public void deleteProduct(UUID id) {
        if (products.remove(id) == null) {
            throw new ProductNotFoundException(id.toString());
        }
    }


    private Double fetchPrice(UUID productId) {
        try {
            Map<String, Object> response = restClient.get()
                    .uri("/prices/{id}", productId.toString())
                    .retrieve()
                    .body(Map.class);
            if (response != null && response.containsKey("price")) {
                return Double.valueOf(response.get("price").toString());
            }
        }
        catch (Exception ex) {
            System.out.println("Price service unavailable: " + ex.getMessage());
        }
        return 0.0;
    }
}
