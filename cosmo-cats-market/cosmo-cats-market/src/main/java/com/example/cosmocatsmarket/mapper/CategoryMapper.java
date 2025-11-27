package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Category;
import com.example.cosmocatsmarket.dto.CategoryDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toCategoryDto(Category category);
    Category toCategory(CategoryDTO dto);
}
