package com.my.store.controller;


import com.my.store.dto.OrderDto;
import com.my.store.model.Cart;
import com.my.store.model.Order;
import com.my.store.service.CartService;
import com.my.store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    public String myOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Order> orders = orderService.findByUser(userDetails.getUsername());
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("orderDto") OrderDto dto,
                           BindingResult result,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model,
                           RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            Cart cart = cartService.getOrCreateCart(userDetails.getUsername());
            model.addAttribute("cart", cart);
            return "order/checkout";
        }

        try {
            Order order = orderService.createFromCart(userDetails.getUsername(), dto);
            redirectAttributes.addFlashAttribute("success", "Заказ #" + order.getId() + " успешно создан");
            return "redirect:/orders/" + order.getId();
        } catch(Exception e) {
            model.addAttribute("error", e.getMessage());
            Cart cart = cartService.getOrCreateCart(userDetails.getUsername());
            model.addAttribute("cart", cart);
            return "order/checkout";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Order order = orderService.findById(id).orElseThrow(() -> new RuntimeException("Заказ не найден"));

        boolean isOwner = order.getUser().getUsername().equals(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "order/detail";
    }

    @GetMapping("/checkout")
    public String checkoutPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Cart cart = cartService.getOrCreateCart(userDetails.getUsername());
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("orderDto", new OrderDto());
        return "order/checkout";
    }
}
