package com.example.coffeeshop.controller;

import com.example.coffeeshop.dto.CreateOrderDto;
import com.example.coffeeshop.dto.DisplayOrderDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Order controller. */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    /** Constructor. */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** Create order. */
    @PostMapping
    public ResponseEntity<DisplayOrderDto> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        DisplayOrderDto createdOrder = orderService.createOrder(createOrderDto);
        if (createdOrder != null) {
            return ResponseEntity.ok(createdOrder);
        }
        return ResponseEntity.badRequest().body(null); // Если пользователь не найден
    }

    /** Delete order. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.deleteOrder(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build(); // Заказ не найден
    }

    /** Get order by id. */
    @GetMapping("/{id}")
    public ResponseEntity<DisplayOrderDto> getOrderById(@PathVariable Long id) {
        DisplayOrderDto displayOrderDto = orderService.getOrderById(id);
        if (displayOrderDto != null) {
            return ResponseEntity.ok(displayOrderDto);
        }
        return ResponseEntity.notFound().build(); // Заказ не найден
    }

    /** Get order by user id. */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DisplayOrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<DisplayOrderDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /** Get all orders by user phone number. */
    @GetMapping("/filter")
    public List<DisplayOrderDto> getOrdersByPhoneNumber(@RequestParam String phoneNumber) {
        return orderService.filterCarsByBrand(phoneNumber);
    }
}
