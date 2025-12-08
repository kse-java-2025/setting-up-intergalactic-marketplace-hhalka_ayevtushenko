package com.example.cosmocatsmarket.IT;

import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import com.example.cosmocatsmarket.service.CategoryDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDbServiceImplIt extends AbstractIt {

    @Autowired
    private CategoryDbService categoryDbService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void cleanDb() {
        categoryRepository.deleteAll();
    }

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


    @Test
    @DisplayName("shouldReturnAllCategories: Find all categories")
    void shouldReturnAllCategories() {
        CategoryEntity c1 = categoryDbService.saveCategory(
                CategoryEntity.builder().name("Food").build());
        CategoryEntity c2 = categoryDbService.saveCategory(
                CategoryEntity.builder().name("Tech").build());

        var all = categoryDbService.findAllCategories();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(c -> c.getId().equals(c1.getId())));
        assertTrue(all.stream().anyMatch(c -> c.getId().equals(c2.getId())));
    }


    @Test
    @DisplayName("shouldUpdateCategory: Update existing category")
    void shouldUpdateCategory() {
        CategoryEntity saved = categoryDbService.saveCategory(
                CategoryEntity.builder().name("Old Name").build());

        CategoryEntity updated = CategoryEntity.builder()
                .name("New Name")
                .build();

        CategoryEntity result = categoryDbService.updateCategory(saved.getId(), updated);
        assertEquals("New Name", result.getName());
    }


    @Test
    @DisplayName("shouldDeleteCategory: Delete existing category")
    void shouldDeleteCategory() {
        CategoryEntity saved = categoryDbService.saveCategory(
                CategoryEntity.builder().name("ToDelete").build());

        categoryDbService.deleteCategory(saved.getId());

        Optional<CategoryEntity> found = categoryDbService.findCategoryById(saved.getId());
        assertTrue(found.isEmpty());
    }

}
