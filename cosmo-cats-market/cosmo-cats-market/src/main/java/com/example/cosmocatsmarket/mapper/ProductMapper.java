package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productId", target = "productId")
    ProductDTO toDto(Product product);

    @Mapping(target = "productId", ignore = true)
    Product fromDTO(ProductDTO dto);
    // Product must have a category
    @Mapping(source = "categoryId", target = "categoryId")
    CategoryDTO toDto(Category category);

    @Mapping(target = "categoryId", ignore = true)
    Category fromDTO(CategoryDTO dto);
}
