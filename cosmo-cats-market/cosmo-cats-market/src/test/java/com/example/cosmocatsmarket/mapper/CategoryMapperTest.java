package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Category;
import com.example.cosmocatsmarket.dto.CategoryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryMapperTest {

    private final CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);

    @Test
    @DisplayName("shouldMapCategoryToDto: Map category to DTO")
    void shouldMapCategoryToDto() {
        Category category = new Category();
        UUID id = UUID.fromString("13841de0-f050-490f-a67c-d80d74e93b11");
        category.setCategoryId(id);
        category.setCategoryName("Snacks");
        CategoryDTO dto = mapper.toDto(category);

        assertNotNull(dto);
        assertEquals(category.getCategoryId().toString(), dto.getCategoryId());
        assertEquals(category.getCategoryName(), dto.getCategoryName());
    }

    @Test
    @DisplayName("shouldMapDtoToCategory: Map DTO to category")
    void shouldMapDtoToCategory() {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId("c3c2cffa-0933-4f6a-8d2d-60696142ced4");
        dto.setCategoryName("Food");
        Category category = mapper.fromDTO(dto);

        assertNotNull(category);
        assertEquals(UUID.fromString(dto.getCategoryId()), category.getCategoryId());
        assertEquals(dto.getCategoryName(), category.getCategoryName());
    }
}
