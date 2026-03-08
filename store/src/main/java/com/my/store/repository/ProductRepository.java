package com.my.store.repository;


import com.my.store.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop8ByActiveTrueOrderByCreatedAtDesc();

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:brand IS NULL OR LOWER(p.brand) = LOWER(:brand))")
    Page<Product> filterProducts(
            @Param("category") Product.Category category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") String brand,
            Pageable pageable);

    Page<Product> findByCategoryAndActiveTrue(Product.Category category, Pageable pageable);

    Page<Product> findByActiveTrue(Pageable pageable);

    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.active = true")
    List<String> findDistinctBrandByActiveTrue();
}