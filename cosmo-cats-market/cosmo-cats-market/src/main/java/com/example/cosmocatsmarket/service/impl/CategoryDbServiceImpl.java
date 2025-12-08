package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import com.example.cosmocatsmarket.service.CategoryDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryDbServiceImpl implements CategoryDbService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryEntity saveCategory(CategoryEntity category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with name '" + category.getName() + "' already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<CategoryEntity> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryEntity> searchCategoriesByName(String namePart) {
        return categoryRepository.findByNameContainingIgnoreCase(namePart);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryEntity updateCategory(Long id, CategoryEntity category) {
        CategoryEntity existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        if (!existingCategory.getName().equals(category.getName())
                && categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with name '" + category.getName() + "' already exists");
        }

        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }


    @Override
    public List<CategoryRepository.CategoryStats> getCategoryStatistics() {
        return categoryRepository.getCategoryStatistics();
    }
}