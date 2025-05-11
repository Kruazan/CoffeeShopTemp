package com.example.coffeeshop.service;

import com.example.coffeeshop.dto.CoffeeDto;
import com.example.coffeeshop.dto.CoffeeUpdateDto;
import com.example.coffeeshop.dto.CoffeeWithOrdersDto;
import com.example.coffeeshop.dto.OrderInfoDto;
import com.example.coffeeshop.mapper.CoffeeMapper;
import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.User;
import com.example.coffeeshop.repository.CoffeeRepository;
import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service. */
@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CoffeeMapper coffeeMapper;
    private final OrderService orderService;

    /** Constructor. */
    @Autowired
    public CoffeeService(CoffeeRepository coffeeRepository, CoffeeMapper coffeeMapper,
                         OrderRepository orderRepository, UserRepository userRepository, OrderService orderService) {
        this.coffeeRepository = coffeeRepository;
        this.coffeeMapper = coffeeMapper;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    /** Get all coffees. */
    public List<CoffeeDto> getAllCoffees() {
        return coffeeRepository.findAll().stream()
                .map(coffeeMapper::toDto)
                .toList();
    }

    /** Get coffee by id. */
    public Optional<CoffeeDto> getCoffeeById(Long id) {
        return coffeeRepository.findById(id).map(coffeeMapper::toDto);
    }

    /** Create coffee. */
    @Transactional
    public CoffeeDto createCoffee(CoffeeDto coffeeDto) {
        // Проверка на существование кофе с таким названием
        Optional<Coffee> existingCoffee = coffeeRepository.findByName(coffeeDto.getName());
        if (existingCoffee.isPresent()) {
            throw new IllegalArgumentException("Кофе с таким названием уже существует");
        }

        Coffee coffee = coffeeMapper.toEntity(coffeeDto);
        return coffeeMapper.toDto(coffeeRepository.save(coffee));
    }

    /** Update coffee. */
    @Transactional
    public CoffeeDto updateCoffee(Long id, CoffeeUpdateDto coffeeDto) {
        return coffeeRepository.findById(id)
                .map(coffee -> {
                    boolean updated = false;

                    if (coffeeDto.getName() != null && !coffeeDto.getName().isBlank()) {
                        coffee.setName(coffeeDto.getName());
                        updated = true;
                    }
                    if (coffeeDto.getType() != null && !coffeeDto.getType().isBlank()) {
                        coffee.setType(coffeeDto.getType());
                        updated = true;
                    }
                    if (coffeeDto.getPrice() != null && coffeeDto.getPrice() != 0) {
                        coffee.setPrice(coffeeDto.getPrice());
                        updated = true;
                    }

                    if (!updated) {
                        throw new IllegalArgumentException("Не передано ни одного корректного поля для обновления");
                    }

                    return coffeeMapper.toDto(coffeeRepository.save(coffee));
                })
                .orElseThrow(() -> new IllegalArgumentException("Кофе с ID " + id + " не найден"));
    }



    /** Delete coffee. */
    @Transactional
    public boolean deleteCoffee(Long id) {
        if (coffeeRepository.existsById(id)) {
            Coffee coffee = coffeeRepository.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("Coffee not found"));

            // Удаление кофе из всех заказов
            for (Order order : coffee.getOrders()) {
                order.getCoffees().remove(coffee);

                // Если это единственный кофе в заказе, удаляем заказ
                if (order.getCoffees().isEmpty()) {
                    orderService.deleteOrder(order.getId());
                } else {
                    orderRepository.save(order);  // Сохраняем изменения в заказах
                }
            }

            // Удаление кофе из избранного у всех пользователей
            for (User user : userRepository.findAll()) {
                user.getFavoriteCoffees().remove(coffee);
                userRepository.save(user);
            }

            // Удаляем кофе из базы данных
            coffeeRepository.deleteById(id);
            return true;
        }
        return false; // Кофе с таким ID не найдено
    }

    @Transactional(readOnly = true)
    public CoffeeWithOrdersDto getCoffeeWithOrders(Long id) {
        Coffee coffee = coffeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coffee not found with id: " + id));

        List<Order> orders = orderRepository.findByCoffeeId(id);

        return new CoffeeWithOrdersDto(
                coffee.getId(),
                coffee.getName(),
                coffee.getType(),
                coffee.getPrice(),
                orders.stream()
                        .map(order -> new OrderInfoDto(
                                order.getId(),
                                order.getUser().getName(),
                                order.getNotes()
                        ))
                        .toList()
        );
    }
}
