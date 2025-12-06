package com.example.cosmocatsmarket.IT;

import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import com.example.cosmocatsmarket.service.CategoryDbService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDbServiceImplIt extends AbstractIt {

    @Autowired
    private CategoryDbService categoryDbService;

    @Test
    @DisplayName("shouldCreateAndReadCategoryFromDb: Create category and read it by id")
    void shouldCreateAndReadCategoryFromDb() {
        CategoryEntity category = CategoryEntity.builder()
                .name("Helmets")
                .build();

        CategoryEntity saved = categoryDbService.saveCategory(category);

        assertNotNull(saved.getId());

        Optional<CategoryEntity> foundOpt = categoryDbService.findCategoryById(saved.getId());
        assertTrue(foundOpt.isPresent());
        assertEquals("Helmets", foundOpt.get().getName());
    }

    @Test
    @DisplayName("shouldFindCategoryByName: Find category by name")
    void shouldFindCategoryByName() {
        CategoryEntity category = CategoryEntity.builder()
                .name("Space Suits")
                .build();
        categoryDbService.saveCategory(category);

        Optional<CategoryEntity> foundOpt = categoryDbService.findCategoryByName("Space Suits");

        assertTrue(foundOpt.isPresent());
        assertEquals("Space Suits", foundOpt.get().getName());
    }
}
