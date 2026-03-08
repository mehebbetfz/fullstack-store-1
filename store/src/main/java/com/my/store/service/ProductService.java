package com.my.store.service;

import com.my.store.model.Product;
import com.my.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product update(Long id, Product updated) {
        Product existing = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Товар не найден"));

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setStockQuantity(updated.getStockQuantity());
        existing.setCategory(updated.getCategory());
        existing.setBrand(updated.getBrand());
        existing.setModel(updated.getModel());
        existing.setImageUrl(updated.getImageUrl());
        existing.setActive(updated.isActive());

        return productRepository.save(existing);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public void toggleActive(Long id) {
        Product existing = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Товар не найден"));

        existing.setActive(!existing.isActive());
        productRepository.save(existing);
    }

    public List<Product> findNewest() {
        return productRepository.findTop8ByActiveTrueOrderByCreatedAtDesc();
    }

    public Page<Product> search(String query, Pageable pageable) {
        return productRepository.searchProducts(query, pageable);
    }

    public Page<Product> findByCategory(Product.Category category, Pageable pageable) {
        return productRepository.findByCategoryAndActiveTrue(category, pageable);
    }

    public Page<Product> filter(Product.Category category, BigDecimal minPrice, BigDecimal maxPrice, String brand, Pageable pageable) {
        return productRepository.filterProducts(category, minPrice, maxPrice, brand, pageable);
    }

    public Page<Product> findAllActive(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable);
    }

    public List<String> findAllBrands() {
        return productRepository.findDistinctBrandByActiveTrue();
    }
}