package com.my.store.controller;

import com.my.store.model.Cart;
import com.my.store.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String view(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Cart cart = cartService.getOrCreateCart(userDetails.getUsername());

        model.addAttribute("cart", cart);
        return "cart/view";
    }

    @PostMapping("/add")
    public String add(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        try {
            cartService.addToCart(userDetails.getUsername(), productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Товар добавлен в корзину");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/products/" + productId;
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        cartService.removeFromCart(userDetails.getUsername(), productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clear(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails.getUsername());
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long productId, @RequestParam int quantity, @AuthenticationPrincipal UserDetails userDetails) {
        cartService.updateQuantity(userDetails.getUsername(), productId, quantity);
        return "redirect:/cart";
    }

}
