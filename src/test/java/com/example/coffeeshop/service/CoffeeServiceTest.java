package com.example.coffeeshop.service;

import com.example.coffeeshop.dto.CoffeeDto;
import com.example.coffeeshop.dto.CoffeeUpdateDto;
import com.example.coffeeshop.mapper.CoffeeMapper;
import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.User;
import com.example.coffeeshop.repository.CoffeeRepository;
import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CoffeeServiceTest {

    @Mock
    private CoffeeRepository coffeeRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CoffeeMapper coffeeMapper;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CoffeeService coffeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCoffees() {
        // Подготовка данных
        Coffee coffee = new Coffee();
        coffee.setId(1L);
        CoffeeDto coffeeDto = new CoffeeDto();
        coffeeDto.setId(1L);

        // Мокаем репозиторий
        when(coffeeRepository.findAll()).thenReturn(Collections.singletonList(coffee));
        when(coffeeMapper.toDto(coffee)).thenReturn(coffeeDto);

        // Вызов метода
        var result = coffeeService.getAllCoffees();

        // Проверка результатов
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testGetCoffeeById_Found() {
        // Подготовка данных
        Coffee coffee = new Coffee();
        coffee.setId(1L);
        CoffeeDto coffeeDto = new CoffeeDto();
        coffeeDto.setId(1L);

        // Мокаем репозиторий
        when(coffeeRepository.findById(1L)).thenReturn(Optional.of(coffee));
        when(coffeeMapper.toDto(coffee)).thenReturn(coffeeDto);

        // Вызов метода
        var result = coffeeService.getCoffeeById(1L);

        // Проверка результатов
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetCoffeeById_NotFound() {
        // Мокаем репозиторий
        when(coffeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Вызов метода
        var result = coffeeService.getCoffeeById(1L);

        // Проверка результатов
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateCoffee_Success() {
        // Подготовка данных
        CoffeeDto coffeeDto = new CoffeeDto();
        coffeeDto.setId(1L);
        coffeeDto.setName("Latte");
        coffeeDto.setType("Espresso");
        coffeeDto.setPrice(5.0);

        Coffee coffee = new Coffee();
        coffee.setId(1L);
        coffee.setName(coffeeDto.getName());
        coffee.setType(coffeeDto.getType());
        coffee.setPrice(coffeeDto.getPrice());

        // Мокаем поведение репозитория и маппера
        when(coffeeRepository.findByName(coffeeDto.getName())).thenReturn(Optional.empty());  // Мокаем отсутствие такого кофе в репозитории
        when(coffeeMapper.toEntity(coffeeDto)).thenReturn(coffee);  // Мокаем преобразование в сущность
        when(coffeeRepository.save(any(Coffee.class))).thenReturn(coffee);  // Мокаем сохранение в репозитории
        when(coffeeMapper.toDto(coffee)).thenReturn(coffeeDto);  // Мокаем преобразование сущности в DTO

        // Вызов метода
        CoffeeDto result = coffeeService.createCoffee(coffeeDto);

        // Логгирование для диагностики
        System.out.println("CoffeeDto result: " + result);  // Проверяем результат

        // Проверки
        assertNotNull(result);  // Проверяем, что результат не null
        assertEquals(1L, result.getId());  // Проверяем, что ID установлен
        assertEquals("Latte", result.getName());  // Проверка на имя кофе

        // Проверка, что save был вызван
        verify(coffeeRepository).save(any(Coffee.class));  // Проверка вызова save
        // Проверка, что toDto был вызван
        verify(coffeeMapper).toDto(any(Coffee.class));  // Проверка вызова toDto
    }


    @Test
    void testCreateCoffee_AlreadyExists() {
        // Подготовка данных
        CoffeeDto coffeeDto = new CoffeeDto();
        coffeeDto.setName("Latte");

        Coffee coffee = new Coffee();
        coffee.setId(1L);
        coffee.setName("Latte");

        // Мокаем репозиторий
        when(coffeeRepository.findByName(any())).thenReturn(Optional.of(coffee));

        // Проверка выброса исключения
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> coffeeService.createCoffee(coffeeDto));
        assertEquals("Кофе с таким названием уже существует", exception.getMessage());
    }

    @Test
    void testUpdateCoffee_Success() {
        // Подготовка данных
        Coffee coffee = new Coffee();
        coffee.setId(1L);
        coffee.setName("Espresso");

        CoffeeUpdateDto updateDto = new CoffeeUpdateDto();
        updateDto.setName("Cappuccino");

        CoffeeDto coffeeDto = new CoffeeDto();
        coffeeDto.setId(1L);
        coffeeDto.setName("Cappuccino");

        // Мокаем репозиторий
        when(coffeeRepository.findById(1L)).thenReturn(Optional.of(coffee));
        when(coffeeRepository.save(any(Coffee.class))).thenReturn(coffee);
        when(coffeeMapper.toDto(coffee)).thenReturn(coffeeDto);

        // Вызов метода
        CoffeeDto result = coffeeService.updateCoffee(1L, updateDto);

        // Проверка результатов
        assertNotNull(result);
        assertEquals("Cappuccino", result.getName());
    }

    @Test
    void testUpdateCoffee_NotFound() {
        // Подготовка данных
        CoffeeUpdateDto updateDto = new CoffeeUpdateDto();
        updateDto.setName("Cappuccino");

        // Мокаем репозиторий
        when(coffeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Проверка выброса исключения
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> coffeeService.updateCoffee(1L, updateDto));
        assertEquals("Кофе с ID 1 не найден", exception.getMessage());
    }

    @Test
    void testDeleteCoffee_Success() {
        // Подготовка данных
        Coffee coffee = new Coffee();
        coffee.setId(1L);
        coffee.setName("Latte");

        // Инициализация заказов для кофе
        Order order = new Order();
        order.setId(1L);
        order.setCoffees(new ArrayList<>(List.of(coffee)));  // Связываем заказ с кофе

        coffee.setOrders(new ArrayList<>(List.of(order)));    // Убедитесь, что заказы не null

        User user = new User();
        user.setFavoriteCoffees(new ArrayList<>(List.of(coffee)));

        // Мокаем репозитории и сервисы
        when(coffeeRepository.existsById(anyLong())).thenReturn(true);
        when(coffeeRepository.findById(anyLong())).thenReturn(Optional.of(coffee));
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Мокаем deleteOrder(), чтобы он возвращал true
        when(orderService.deleteOrder(anyLong())).thenReturn(true);

        // Вызов метода
        boolean result = coffeeService.deleteCoffee(1L);

        // Проверка, что удаление прошло успешно
        assertTrue(result);

        // Проверяем, что deleteOrder() был вызван для заказа
        verify(orderService).deleteOrder(anyLong());
    }





    @Test
    void testDeleteCoffee_NotFound() {
        // Мокаем репозиторий
        when(coffeeRepository.existsById(1L)).thenReturn(false);

        // Вызов метода
        boolean result = coffeeService.deleteCoffee(1L);

        // Проверка результатов
        assertFalse(result);
    }
}
