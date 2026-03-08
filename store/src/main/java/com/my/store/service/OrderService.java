package com.my.store.service;

import com.my.store.dto.OrderDto;
import com.my.store.model.*;
import com.my.store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;

    public Order createFromCart(String username, OrderDto dto) {
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Cart cart = cartService.getOrCreateCart(username);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Корзина пуста");
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(dto.getShippingAddress());
        order.setPhone(dto.getPhone());
        order.setNotes(dto.getNotes());
        order.setStatus(Order.OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItems.add(orderItem);
            total = total.add(cartItem.getSubtotal());
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        cartService.clearCart(username);
        return saved;
    }

    public List<Order> findByUser(String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Optional<Order> findById(Long id) {
        return  orderRepository.findById(id);
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public long countByStatus(Order.OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    public void updateStatus(Long orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Заказ не найден"));

        order.setStatus(newStatus);
        orderRepository.save(order);
    }
}
