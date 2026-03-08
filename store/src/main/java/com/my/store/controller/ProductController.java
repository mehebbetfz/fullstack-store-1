package com.my.store.controller;

import com.my.store.model.Product;
import com.my.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String brand,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String dir,
            Model model
    ) {
        Sort.Direction direction = dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Product> products;

        if (search != null && !search.isBlank()) {
            products = productService.search(search, pageable);
            model.addAttribute("search", search);
        } else if (category != null && !category.isBlank()) {
            products = productService.findByCategory(Product.Category.valueOf(category), pageable);
            model.addAttribute("selectedCategory", category);
        } else if (minPrice != null || maxPrice != null || brand != null) {
            Product.Category cat = category != null && !category.isBlank() ? Product.Category.valueOf(category) : null;

            products = productService.filter(cat, minPrice, maxPrice, brand, pageable);
        } else {
            products = productService.findAllActive(pageable);
        }


        model.addAttribute("products", products);
        model.addAttribute("categories", Product.Category.values());
        model.addAttribute("brands", productService.findAllBrands());
        model.addAttribute("currentPage", page);
        model.addAttribute("sortField", sort);
        model.addAttribute("sortDir", dir);


        return "product/list";
    }
}
