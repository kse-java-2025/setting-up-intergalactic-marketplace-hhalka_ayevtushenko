package com.example.cosmocatsmarket.repository;

import com.example.cosmocatsmarket.repository.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT c.name as categoryName, COUNT(p.id) as productCount " +
            "FROM CategoryEntity c " +
            "LEFT JOIN ProductEntity p ON c.id = p.category.id " +
            "GROUP BY c.id, c.name " +
            "ORDER BY productCount DESC")

    List<CategoryStats> getCategoryStatistics();

    interface CategoryStats {
        String getCategoryName();
        Long getProductCount();
    }

    List<CategoryEntity> findByNameContainingIgnoreCase(String namePart);
    List<CategoryEntity> findByNameStartingWithIgnoreCase(String prefix);
}