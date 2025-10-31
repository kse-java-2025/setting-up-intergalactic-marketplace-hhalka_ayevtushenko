package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.dto.ProductDTO;
import java.util.*;

public interface ProductService {
    List<ProductDTO> getAll();
    Optional<ProductDTO> getById(UUID id);
    ProductDTO create(ProductDTO dto);
    Optional<ProductDTO> update(UUID id, ProductDTO dto);
    boolean delete(UUID id);
}