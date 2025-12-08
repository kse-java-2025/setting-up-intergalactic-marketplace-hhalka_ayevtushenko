package com.example.cosmocatsmarket.repository;

import com.example.cosmocatsmarket.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByName(String name);
    List<ProductEntity> findByCategoryId(Long categoryId);
    List<ProductEntity> findByStatus(Boolean status);
    List<ProductEntity> findByPriceBetween(Double minPrice, Double maxPrice);
    List<ProductEntity> findByNameContainingIgnoreCase(String name);
    List<ProductEntity> findByStatusTrueOrderByPriceDesc();
    List<ProductEntity> findByCategoryIdAndStatusTrueOrderByNameAsc(Long categoryId);
    List<ProductEntity> findByPriceGreaterThanOrderByPriceAsc(Double minPrice);
    boolean existsByNameAndCategoryId(String name, Long categoryId);


    @Query("SELECT p.name as productName, p.price as price, " +
            "COALESCE(SUM(oi.quantity), 0) as totalSold, " +
            "COALESCE(COUNT(oi.id), 0) as orderCount " +
            "FROM ProductEntity p " +
            "LEFT JOIN OrderItemEntity oi ON p.id = oi.product.id " +
            "GROUP BY p.id, p.name, p.price " +
            "ORDER BY totalSold DESC")

    List<ProductSalesReport> getTopSellingProducts();

    interface ProductSalesReport {
        String getProductName();
        Double getPrice();
        Long getTotalSold();
        Long getOrderCount();
    }

    @Query("SELECT c.name as categoryName, p.name as productName, p.price as price, p.status as active " +
            "FROM ProductEntity p " +
            "JOIN CategoryEntity c ON p.category.id = c.id " +
            "WHERE p.status = true " +
            "ORDER BY c.name ASC, p.price DESC")
    List<ProductCategoryReport> getActiveProductsByCategory();


    interface ProductCategoryReport {
        String getCategoryName();
        String getProductName();
        Double getPrice();
        Boolean getActive();
    }

    @Query("SELECT c.name as categoryName, " +
            "COUNT(p.id) as productCount, " +
            "MIN(p.price) as minPrice, " +
            "MAX(p.price) as maxPrice, " +
            "AVG(p.price) as avgPrice " +
            "FROM ProductEntity p " +
            "JOIN CategoryEntity c ON p.category.id = c.id " +
            "WHERE p.status = true " +
            "GROUP BY c.id, c.name " +
            "ORDER BY productCount DESC")
    List<CategoryPriceStats> getCategoryPriceStatistics();


    interface CategoryPriceStats {
        String getCategoryName();
        Long getProductCount();
        Double getMinPrice();
        Double getMaxPrice();
        Double getAvgPrice();
    }

    @Query("SELECT p FROM ProductEntity p " +
            "WHERE p.name LIKE %:keyword% " +
            "AND p.status = true " +
            "ORDER BY p.price ASC")
    List<ProductEntity> searchActiveProducts(@Param("keyword") String keyword);


    @Query("SELECT p.name as productName, p.price as price, c.name as categoryName " +
            "FROM ProductEntity p " +
            "JOIN CategoryEntity c ON p.category.id = c.id " +
            "WHERE p.price > :minPrice " +
            "AND p.status = true " +
            "ORDER BY p.price DESC")
    List<ExpensiveProductReport> getExpensiveProducts(@Param("minPrice") Double minPrice);


    interface ExpensiveProductReport {
        String getProductName();
        Double getPrice();
    }

    @Query("SELECT COUNT(p) as totalProducts, " +
            "SUM(CASE WHEN p.status = true THEN 1 ELSE 0 END) as activeProducts, " +
            "AVG(p.price) as averagePrice, " +
            "SUM(p.price) as totalValue " +
            "FROM ProductEntity p")
    ProductStatistics getProductStatistics();

    interface ProductStatistics {
        Long getTotalProducts();
        Long getActiveProducts();
        Double getAveragePrice();
        Double getTotalValue();
    }
}