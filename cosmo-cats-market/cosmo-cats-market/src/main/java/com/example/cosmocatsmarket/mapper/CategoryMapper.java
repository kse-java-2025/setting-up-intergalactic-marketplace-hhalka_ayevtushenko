package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Category;
import com.example.cosmocatsmarket.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDto(Category category);

    Category fromDTO(CategoryDTO dto);
}
