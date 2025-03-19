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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service. */
@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CoffeeRepository coffeeRepository;
    private final Map<String, List<DisplayOrderDto>> orderFilterCache;

    /** Constructor. */
    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderMapper orderMapper,
                        CoffeeRepository coffeeRepository, Map<String, List<DisplayOrderDto>> orderFilterCache) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.coffeeRepository = coffeeRepository;
        this.orderFilterCache = orderFilterCache;
    }

    /** Create order. */
    @Transactional
    public DisplayOrderDto createOrder(CreateOrderDto createOrderDto) {
        User user = userRepository.findById(createOrderDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (createOrderDto.getCoffeesIds() == null || createOrderDto.getCoffeesIds().isEmpty()) {
            throw new EntityNotFoundException("Coffees not found");
        }
        List<Coffee> coffees = createOrderDto.getCoffeesIds().stream()
                .map(coffeeId -> coffeeRepository.findById(coffeeId)
                        .orElseThrow(() -> new EntityNotFoundException("Coffee with id " + coffeeId + " not found"))
                )
                .toList();

        Order order = new Order();
        order.setUser(user);
        order.setNotes(createOrderDto.getNotes());
        order.setCoffees(coffees);

        Order savedOrder = orderRepository.save(order);
        DisplayOrderDto savedDto = orderMapper.toDisplayDto(savedOrder);

        log.info("[CACHE]: New order created. Clearing affected cache for user phoneNumber={}", user.getPhoneNumber());
        clearCacheForValue(user.getPhoneNumber());

        return savedDto;
    }

    /** Delete order. */
    @Transactional
    public boolean deleteOrder(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            String phoneNumber = order.getUser().getPhoneNumber();

            orderRepository.deleteById(id);

            log.info("[CACHE]: Order deleted (ID={}). Clearing affected cache for user phoneNumber={}", id, phoneNumber);
            clearCacheForValue(phoneNumber);

            return true;
        }
        return false; // Заказ с таким ID не найден
    }

    /** Get order by id. */
    public DisplayOrderDto getOrderById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        return orderOpt.map(orderMapper::toDisplayDto).orElse(null); // Если заказ найден, конвертируем в DTO
    }

    /** Get all orders. */
    public List<DisplayOrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orderMapper.toDisplayDto(orders);
    }

    /** Filter by user. */
    public List<DisplayOrderDto> filterCarsByBrand(String phoneNumber) {
        if (orderFilterCache.containsKey(phoneNumber)) {
            log.info("[CACHE]: Cache hit for filter: brand='{}'", phoneNumber);
            return orderFilterCache.get(phoneNumber);
        }

        log.info("[CACHE]: Cache miss for filter: brand='{}'. Querying DB.", phoneNumber);

        List<DisplayOrderDto> filteredOrders = orderRepository.findAllByUserPhoneNumber(phoneNumber)
                .stream()
                .map(orderMapper::toDisplayDto)
                .toList();

        orderFilterCache.put(phoneNumber, filteredOrders);
        log.info("[CACHE]: Cache populated for filter: brand='{}'", phoneNumber);

        return filteredOrders;
    }

    /** Очистка кэша по номеру телефона юзера. */
    private void clearCacheForValue(String phoneNumber) {
        if (phoneNumber != null) {
            orderFilterCache.remove(phoneNumber);
        }
    }
}
