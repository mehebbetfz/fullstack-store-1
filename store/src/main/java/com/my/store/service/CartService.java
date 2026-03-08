package com.my.store.service;

import com.my.store.model.Cart;
import com.my.store.model.CartItem;
import com.my.store.model.Product;
import com.my.store.model.User;
import com.my.store.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;

    public Cart getOrCreateCart(String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    public void addToCart(String username, Long productId, int quantity) {
        Cart cart = getOrCreateCart(username);
        Product product = productService.findById(productId).orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(Math.min(item.getQuantity() + quantity, product.getStockQuantity())),
                        () -> {
                            CartItem newItem = new CartItem();
                            newItem.setCart(cart);
                            newItem.setProduct(product);
                            newItem.setQuantity(quantity);
                            cart.getItems().add(newItem);
                        }
                );

        cartRepository.save(cart);
    }

    public void removeFromCart(String username, Long productId) {
        Cart  cart = getOrCreateCart(username);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }

    public void clearCart(String username) {
        Cart  cart = getOrCreateCart(username);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public void updateQuantity(String username, Long productId, int quantity) {
        Cart  cart = getOrCreateCart(username);

        if (quantity <= 0) {
            removeFromCart(username, productId);
            return;
        }

        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        cartRepository.save(cart);
    }
}
