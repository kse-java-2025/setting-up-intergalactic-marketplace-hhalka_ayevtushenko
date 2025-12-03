package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import java.util.List;
import java.util.Optional;

public interface CategoryDbService {
    CategoryEntity saveCategory(CategoryEntity category);
    Optional<CategoryEntity> findCategoryById(Long id);
    Optional<CategoryEntity> findCategoryByName(String name);
    List<CategoryEntity> findAllCategories();
    List<CategoryEntity> searchCategoriesByName(String namePart);
    void deleteCategory(Long id);
    CategoryEntity updateCategory(Long id, CategoryEntity category);
    List<CategoryRepository.CategoryStats> getCategoryStatistics();
}