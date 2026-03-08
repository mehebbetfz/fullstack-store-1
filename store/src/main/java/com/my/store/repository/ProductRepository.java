package com.my.store.repository;


import com.my.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop8ByActiveTrueOrderByCreatedAtDesc();
}
