package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product);
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "review", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product fromDTO(ProductDTO dto);

    default Product toNewProduct(ProductDTO dto, UUID id) {
        return fromDTO(dto)
                .toBuilder()
                .productId(id)
                .build();
    }

    default Product toUpdatedProduct(ProductDTO dto, UUID id) {
        return fromDTO(dto)
                .toBuilder()
                .productId(id)
                .build();
    }
}
