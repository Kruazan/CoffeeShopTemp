package com.example.coffeeshop.service;

import com.example.coffeeshop.dto.CoffeeDto;
import com.example.coffeeshop.dto.DisplayUserDto;
import com.example.coffeeshop.mapper.OrderMapper;
import com.example.coffeeshop.model.Coffee;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.repository.UserRepository;
import com.example.coffeeshop.repository.CoffeeRepository;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.User;
import com.example.coffeeshop.dto.CreateOrderDto;
import com.example.coffeeshop.dto.DisplayOrderDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CoffeeRepository coffeeRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private Map<String, Object> orderFilterCache;

    @Test
    void testCreateOrder_UserNotFound_ShouldThrowException() {
        CreateOrderDto createOrderDto = new CreateOrderDto();
        createOrderDto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(createOrderDto);
        });
    }

    @Test
    void testCreateOrder_OrderCreated_ShouldReturnDisplayOrderDto() {
        // Arrange: Создание входных данных для теста
        CreateOrderDto createOrderDto = new CreateOrderDto();
        createOrderDto.setUserId(1L); // Пользователь с ID = 1
        createOrderDto.setCoffeesIds(List.of(1L, 2L)); // Список из двух кофе с ID = 1 и 2
        createOrderDto.setNotes("f");

        // Моделируем пользователя, который будет найден в репозитории
        User user = new User();
        user.setId(1L);
        user.setPhoneNumber("1234567890");
        user.setName("John Doe");

        // Моделируем кофе, который будет найден в репозитории
        Coffee coffee1 = new Coffee();
        coffee1.setId(1L);
        coffee1.setName("Espresso");
        coffee1.setPrice(2.5);

        Coffee coffee2 = new Coffee();
        coffee2.setId(2L);
        coffee2.setName("Latte");
        coffee2.setPrice(3.0);

        // Создаем заказ (Order), который будет сохранен
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setCoffees(List.of(coffee1, coffee2));
        order.setNotes("f"); // Сумма заказа: 2.5 (Espresso) + 3.0 (Latte)

        // Настройка моков
// Настройка моков
        DisplayUserDto displayUserDto = new DisplayUserDto();
        displayUserDto.setId(user.getId());
        displayUserDto.setPhoneNumber(user.getPhoneNumber());
        displayUserDto.setName(user.getName());

        CoffeeDto coffeeDto1 = new CoffeeDto();
        coffeeDto1.setId(coffee1.getId());
        coffeeDto1.setName(coffee1.getName());
        coffeeDto1.setPrice(coffee1.getPrice());

        CoffeeDto coffeeDto2 = new CoffeeDto();
        coffeeDto2.setId(coffee2.getId());
        coffeeDto2.setName(coffee2.getName());
        coffeeDto2.setPrice(coffee2.getPrice());

        DisplayOrderDto displayOrderDto = new DisplayOrderDto();
        displayOrderDto.setId(1L);
        displayOrderDto.setUser(displayUserDto);
        displayOrderDto.setCoffees(List.of(coffeeDto1, coffeeDto2));
        displayOrderDto.setNotes("f");

        when(orderMapper.toDisplayDto(order)).thenReturn(displayOrderDto);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(coffeeRepository.findById(1L)).thenReturn(Optional.of(coffee1));
        when(coffeeRepository.findById(2L)).thenReturn(Optional.of(coffee2));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act: Вызов метода, который тестируется
        DisplayOrderDto result = orderService.createOrder(createOrderDto);

        // Assert: Проверка результатов
        assertNotNull(result); // Результат не должен быть null
        assertEquals(1L, result.getId()); // ID заказа должен быть 1
        assertNotNull(result.getUser()); // Проверка, что пользователь не null
        assertEquals(1L, result.getUser().getId()); // ID пользователя должен быть 1
        assertEquals("John Doe", result.getUser().getName()); // Имя пользователя должно быть "John Doe"
        assertEquals("1234567890", result.getUser().getPhoneNumber()); // Номер телефона пользователя должен быть "1234567890"
        assertEquals(2, result.getCoffees().size()); // Должно быть два кофе в заказе

        // Проверка кофе в заказе
        assertEquals(1L, result.getCoffees().get(0).getId()); // ID первого кофе
        assertEquals("Espresso", result.getCoffees().get(0).getName()); // Название первого кофе
        assertEquals(2.5, result.getCoffees().get(0).getPrice()); // Цена первого кофе

        assertEquals(2L, result.getCoffees().get(1).getId()); // ID второго кофе
        assertEquals("Latte", result.getCoffees().get(1).getName()); // Название второго кофе
        assertEquals(3.0, result.getCoffees().get(1).getPrice()); // Цена второго кофе

        // Проверка комов
        assertEquals("f", result.getNotes()); // Общая сумма должна быть 5.5
    }

    @Test
    void testDeleteOrder_OrderExists_ShouldReturnTrue() {
        // Arrange: Моделируем заказ с ID = 1
        Order order = new Order();
        order.setId(1L);
        User user = new User();
        user.setPhoneNumber("1234567890");
        order.setUser(user);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Мокаем удаление
        doNothing().when(orderRepository).deleteById(1L);

        // Act: Вызов метода
        boolean result = orderService.deleteOrder(1L);

        // Assert: Проверка, что заказ удален
        assertTrue(result);
        verify(orderRepository).deleteById(1L); // Убедиться, что метод deleteById был вызван
    }

    @Test
    void testDeleteOrder_OrderNotFound_ShouldReturnFalse() {
        // Arrange: Заказ не найден
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Вызов метода
        boolean result = orderService.deleteOrder(1L);

        // Assert: Проверка, что метод вернул false
        assertFalse(result);
    }

    @Test
    void testGetOrderById_OrderExists_ShouldReturnDisplayOrderDto() {
        // Arrange: Моделируем заказ с ID = 1
        Order order = new Order();
        order.setId(1L);
        User user = new User();
        user.setPhoneNumber("1234567890");
        order.setUser(user);

        // Мокаем вызов метода
        DisplayOrderDto displayOrderDto = new DisplayOrderDto();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDisplayDto(order)).thenReturn(displayOrderDto);

        // Act: Вызов метода
        DisplayOrderDto result = orderService.getOrderById(1L);

        // Assert: Проверка результата
        assertNotNull(result);
    }

    @Test
    void testGetOrderById_OrderNotFound_ShouldReturnNull() {
        // Arrange: Заказ не найден
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Вызов метода
        DisplayOrderDto result = orderService.getOrderById(1L);

        // Assert: Проверка, что метод вернул null
        assertNull(result);
    }

    @Test
    void testGetOrdersByUserId_OrdersExist_ShouldReturnDisplayOrderDtos() {
        // Arrange: Моделируем пользователя с ID = 1
        User user = new User();
        user.setId(1L);

        Order order1 = new Order();
        order1.setUser(user);

        Order order2 = new Order();
        order2.setUser(user);

        List<Order> orders = List.of(order1, order2);

        // Мокаем вызов метода
        when(orderRepository.findByUserId(1L)).thenReturn(orders);
        List<DisplayOrderDto> displayOrderDtos = List.of(new DisplayOrderDto(), new DisplayOrderDto());
        when(orderMapper.toDisplayDto(orders)).thenReturn(displayOrderDtos);

        // Act: Вызов метода
        List<DisplayOrderDto> result = orderService.getOrdersByUserId(1L);

        // Assert: Проверка результата
        assertNotNull(result);
        assertEquals(2, result.size()); // Два заказа
    }

    @Test
    void testGetOrdersByUserId_NoOrders_ShouldReturnEmptyList() {
        // Arrange: У пользователя нет заказов
        when(orderRepository.findByUserId(1L)).thenReturn(List.of());

        // Act: Вызов метода
        List<DisplayOrderDto> result = orderService.getOrdersByUserId(1L);

        // Assert: Проверка, что вернулся пустой список
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterCarsByBrand_CacheHit_ShouldReturnFromCache() {
        // Arrange: Мок кэша
        List<DisplayOrderDto> cachedOrders = List.of(new DisplayOrderDto());
        when(orderFilterCache.containsKey("1234567890")).thenReturn(true);
        when(orderFilterCache.get("1234567890")).thenReturn(cachedOrders);

        // Act: Вызов метода
        List<DisplayOrderDto> result = orderService.filterCarsByBrand("1234567890");

        // Assert: Проверка, что результат пришел из кэша
        assertSame(cachedOrders, result);
    }
}
