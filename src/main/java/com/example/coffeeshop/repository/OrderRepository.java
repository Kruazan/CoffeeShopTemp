package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repos. */
public interface OrderRepository extends JpaRepository<Order, Long> {
    /** Find by user id. */
    List<Order> findByUserId(Long userId); // Получение всех заказов пользователя
}
