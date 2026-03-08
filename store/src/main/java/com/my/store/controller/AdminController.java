package com.my.store.controller;

import com.my.store.model.Product;
import com.my.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;

    @GetMapping("/products")
    public String productList(@RequestParam(defaultValue = "0") int page, Model model) {

        model.addAttribute(
                "products",
                productService.findAll(
                        PageRequest.of(
                                page,
                                20,
                                Sort.by(Sort.Direction.DESC, "createdAt")
                        )
                )
        );

        model.addAttribute("currentPage", page);

        return "admin/products";
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", Product.Category.values());
        return "admin/product-form";
    }

    @PostMapping("/products/new")
    public String createProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Product.Category.values());
            return "admin/product-form";
        }

        productService.create(product);
        redirectAttributes.addFlashAttribute("success", "Товар успешно создан");


        return "redirect:/admin/products";
    }

    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id).orElseThrow(() -> new RuntimeException("Товар не найден"));
        model.addAttribute("product", product);
        model.addAttribute("categories", Product.Category.values());
        return "admin/product-form";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("product") Product product, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Product.Category.values());
            return "admin/product-form";
        }

        productService.update(id, product);
        redirectAttributes.addFlashAttribute("success", "Товар обновлен");

        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Товар удален");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/toggle")
    public String toggleProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.toggleActive(id);
        redirectAttributes.addFlashAttribute("success", "Статус товара изменен");
        return "redirect:/admin/products";
    }

}