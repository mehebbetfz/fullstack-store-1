package com.my.store.controller;

import com.my.store.model.Order;
import com.my.store.model.Product;
import com.my.store.service.OrderService;
import com.my.store.service.ProductService;
import com.my.store.service.UserService;
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
    private final OrderService orderService;
    private final UserService userService;



    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalProducts", productService.findAll(PageRequest.of(0, 1)).getTotalElements());
        model.addAttribute("totalOrders", orderService.findAll(PageRequest.of(0, 1)).getTotalElements());
        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("pendingOrders", orderService.countByStatus(Order.OrderStatus.PENDING));
        model.addAttribute("newestProducts", productService.findNewest());
        model.addAttribute("recentOrders", orderService.findAll(PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "createdAt"))).getContent());

        return "admin/dashboard";
    }



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

    @GetMapping("/orders")
    public String orderList(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("orders", orderService.findAll(
                PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"))));

        model.addAttribute("currentPage", page);
        model.addAttribute("statuses", Order.OrderStatus.values());
        return "admin/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id).orElseThrow(() -> new RuntimeException("Заказ не найден")));
        model.addAttribute("statuses", Order.OrderStatus.values());
        return "admin/order-detail";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes
    ) {
        orderService.updateStatus(id, Order.OrderStatus.valueOf(status));
        redirectAttributes.addFlashAttribute("success", "Статус заказа обновлен");
        return "redirect:/admin/orders/" + id;
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(
            @PathVariable Long id, RedirectAttributes redirectAttributes
    ) {
        userService.toggleUserStatus(id);
        redirectAttributes.addFlashAttribute("success", "Статус пользователя изменен");
        return "redirect:/admin/users";
    }

}