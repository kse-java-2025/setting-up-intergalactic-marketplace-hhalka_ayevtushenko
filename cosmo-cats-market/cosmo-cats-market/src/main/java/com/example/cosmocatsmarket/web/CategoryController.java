package com.example.cosmocatsmarket.web;

import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import com.example.cosmocatsmarket.service.CategoryDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryDbService categoryDbService;

    @GetMapping
    public List<CategoryEntity> getAllCategories() {
        return categoryDbService.findAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryEntity> getCategoryById(@PathVariable Long id) {
        return categoryDbService.findCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryEntity> getCategoryByName(@PathVariable String name) {
        return categoryDbService.findCategoryByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<CategoryEntity> searchCategories(@RequestParam String namePart) {
        return categoryDbService.searchCategoriesByName(namePart);
    }

    @GetMapping("/reports/stats")
    public List<CategoryRepository.CategoryStats> getCategoryStatistics() {
        return categoryDbService.getCategoryStatistics();
    }

    @PostMapping
    public CategoryEntity createCategory(@RequestBody CategoryEntity category) {
        return categoryDbService.saveCategory(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryEntity> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryEntity category) {
        try {
            CategoryEntity updated = categoryDbService.updateCategory(id, category);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryDbService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}