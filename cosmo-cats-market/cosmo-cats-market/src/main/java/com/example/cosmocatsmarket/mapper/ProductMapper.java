package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.*;

import org.mapstruct.*
import java.util.*

public interface ProductMapper {

    ProductDTO toDto(Product product);
    List<ProductDTO> toDtoList(List<Product> products);

    @Mapping(target = "categories", ignore = true)
    Product fromDTO(ProductDTO dto);
    List<Product> toEntityList(List<ProductDTO> dtoList);

    String toStrFromUuid(UUID id) { // Converting string from UUID
        if (id == null) {
            return null;
        } else {
            return id.toString();
        }
    }

    UUID toUuidFromStr(String s) { // Converting UUID from string
        if (id == null) {
            return null;
        } else {
            return id.fromString();
        }
    }
}