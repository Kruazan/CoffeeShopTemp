package com.example.coffeeshop.controller;

import com.example.coffeeshop.dto.CreateOrderDto;
import com.example.coffeeshop.dto.DisplayOrderDto;
import com.example.coffeeshop.dto.OrderDto;
import com.example.coffeeshop.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Order controller. */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Создание нового заказа
    @PostMapping
    public ResponseEntity<DisplayOrderDto> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        DisplayOrderDto createdOrder = orderService.createOrder(createOrderDto);
        if (createdOrder != null) {
            return ResponseEntity.ok(createdOrder);
        }
        return ResponseEntity.badRequest().body(null); // Если пользователь не найден
    }

    // Удаление заказа
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.deleteOrder(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build(); // Заказ не найден
    }

    // Получение заказа по ID
    @GetMapping("/{id}")
    public ResponseEntity<DisplayOrderDto> getOrderById(@PathVariable Long id) {
        DisplayOrderDto displayOrderDto = orderService.getOrderById(id);
        if (displayOrderDto != null) {
            return ResponseEntity.ok(displayOrderDto);
        }
        return ResponseEntity.notFound().build(); // Заказ не найден
    }

    // Получение всех заказов пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DisplayOrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<DisplayOrderDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}
