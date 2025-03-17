package com.example.coffeeshop.service;

import com.example.coffeeshop.dto.CreateOrderDto;
import com.example.coffeeshop.dto.DisplayOrderDto;
import com.example.coffeeshop.mapper.OrderMapper;
import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.User;
import com.example.coffeeshop.repository.CoffeeRepository;
import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CoffeeRepository coffeeRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderMapper orderMapper, CoffeeRepository coffeeRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.coffeeRepository = coffeeRepository;
    }

    // Создание нового заказа
    public DisplayOrderDto createOrder(CreateOrderDto createOrderDto) {
        // Ищем пользователя
        User user = userRepository.findById(createOrderDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Ищем кофе
        if (createOrderDto.getCoffeesIds() == null || createOrderDto.getCoffeesIds().isEmpty()) {
            throw new EntityNotFoundException("Coffees not found");
        }
        List<Coffee> coffees = createOrderDto.getCoffeesIds().stream()
                .map(coffeeId -> coffeeRepository.findById(coffeeId)
                        .orElseThrow(() -> new EntityNotFoundException("Coffee with id " + coffeeId + " not found"))
                )
                .toList();

        // Создаём заказ
        Order order = new Order();
        order.setUser(user);
        order.setNotes(createOrderDto.getNotes());
        order.setCoffees(coffees); // Добавляем один кофе (если нужен список кофе - можно изменить dto)

        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);

        // Возвращаем DTO
        return orderMapper.toDisplayDto(savedOrder);
    }


    // Удаление заказа
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false; // Заказ с таким ID не найден
    }

    // Получение заказа по ID
    public DisplayOrderDto getOrderById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        return orderOpt.map(orderMapper::toDisplayDto).orElse(null); // Если заказ найден, конвертируем в DTO
    }

    // Получение всех заказов пользователя
    public List<DisplayOrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orderMapper.toDisplayDto(orders);
    }
}
