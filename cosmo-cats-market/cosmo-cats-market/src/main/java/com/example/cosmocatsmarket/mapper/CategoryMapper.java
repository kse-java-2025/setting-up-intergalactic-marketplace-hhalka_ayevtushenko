package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Category;
import com.example.cosmocatsmarket.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "categoryId", target = "categoryId")
    CategoryDTO toDto(Category category);

    @Mapping(target = "categoryId", ignore = true)
    Category fromDTO(CategoryDTO dto);
}
