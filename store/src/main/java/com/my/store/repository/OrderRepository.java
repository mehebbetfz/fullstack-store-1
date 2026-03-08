package com.my.store.repository;

import com.my.store.model.Order;
import com.my.store.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
    long countByStatus(Order.OrderStatus status);
    List<Order> findByStatus(Order.OrderStatus status);
}
