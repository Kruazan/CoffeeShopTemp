package com.example.coffeeshop.service;

import com.example.coffeeshop.dto.UserUpdateDto;
import com.example.coffeeshop.exception.UserUpdateException;
import com.example.coffeeshop.mapper.UserMapper;
import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.repository.CoffeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.example.coffeeshop.repository.UserRepository;
import com.example.coffeeshop.dto.UserDto;
import com.example.coffeeshop.model.User;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CoffeeRepository coffeeRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser_ShouldReturnUserDto() throws NoSuchAlgorithmException {
        // Подготовка данных для теста
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setPasswordHash("password");

        User user = new User();
        user.setId(1L);

        // Мокаем вызов репозитория
        userDto.setId(1L);
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Вызов метода
        UserDto result = userService.createUser(userDto);

        // Проверка результатов
        assertNotNull(result, "Результат не должен быть null");
        assertEquals(1L, result.getId(), "ID пользователя должен быть 1");
    }

    @Test
    void testCreateUser_ShouldThrowException_WhenUserDtoIsNull() {
        // Проверка на NullPointerException для null userDto
        assertThrows(NullPointerException.class, () -> userService.createUser(null), "Метод должен выбросить NullPointerException при передаче null");
    }

    @Test
    void testDeleteUser_UserNotFound_ShouldReturnFalse() {
        when(userRepository.existsById(1L)).thenReturn(false);

        boolean result = userService.deleteUser(1L);

        assertFalse(result, "Результат должен быть false, если пользователь не найден");
    }

    @Test
    void testDeleteUser_UserFound_ShouldReturnTrue() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result, "Результат должен быть true, если пользователь найден");
    }

    @Test
    void testUpdateUser_ShouldReturnUpdatedUserDto() throws NoSuchAlgorithmException {
        // Подготовка данных для теста
        Long userId = 1L;
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("Jane Doe");
        userUpdateDto.setPhoneNumber("1234567890");
        userUpdateDto.setPasswordHash("newpassword");

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setPhoneNumber("0987654321");
        user.setPassword("oldpassword");

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(userId);
        updatedUserDto.setName("Jane Doe");
        updatedUserDto.setPhoneNumber("1234567890");

        // Мокаем поведение репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(updatedUserDto);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // В ручном порядке мокаем преобразование UserUpdateDto в User
        // Преобразование без использования метода toEntity (можно добавить этот метод в маппер позже, если необходимо)
        user.setName(userUpdateDto.getName());
        user.setPhoneNumber(userUpdateDto.getPhoneNumber());
        try {
            user.setPassword(userService.hashPassword(userUpdateDto.getPasswordHash()));  // Пример хеширования пароля
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Вызов метода
        UserDto result = userService.updateUser(userId, userUpdateDto);

        // Проверка результатов
        assertNotNull(result, "Результат не должен быть null");
        assertEquals("Jane Doe", result.getName(), "Имя должно быть обновлено");
        assertEquals("1234567890", result.getPhoneNumber(), "Номер телефона должен быть обновлен");
    }


    @Test
    void testUpdateUser_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("Jane Doe");

        // Мокаем поведение репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Проверка на исключение
        assertThrows(UserUpdateException.class, () -> userService.updateUser(userId, userUpdateDto),
                "Метод должен выбросить исключение, если пользователь не найден");
    }

    @Test
    void testUpdateUser_ShouldThrowException_WhenPhoneNumberIsBlank() {
        Long userId = 1L;
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setPhoneNumber("");

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        // Мокаем поведение репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Проверка на исключение
        assertThrows(UserUpdateException.class, () -> userService.updateUser(userId, userUpdateDto),
                "Метод должен выбросить исключение, если номер телефона пустой");
    }

    @Test
    void testAddCoffeeToFavorites_ShouldAddCoffee() {
        Long userId = 1L;
        Long coffeeId = 1L;

        User user = new User();
        user.setId(userId);

        Coffee coffee = new Coffee();
        coffee.setId(coffeeId);

        // Мокаем поведение репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(coffee));

        // Вызов метода
        userService.addCoffeeToFavorites(userId, coffeeId);

        // Проверка, что кофе добавлено
        assertTrue(user.getFavoriteCoffees().contains(coffee), "Кофе должно быть добавлено в избранное");
    }

    @Test
    void testAddCoffeeToFavorites_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        Long coffeeId = 1L;

        // Мокаем поведение репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Проверка на исключение
        assertThrows(IllegalArgumentException.class, () -> userService.addCoffeeToFavorites(userId, coffeeId),
                "Метод должен выбросить исключение, если пользователь не найден");
    }

    @Test
    void testRemoveCoffeeFromFavorites_ShouldRemoveCoffee() {
        Long userId = 1L;
        Long coffeeId = 1L;

        User user = new User();
        user.setId(userId);
        Coffee coffee = new Coffee();
        coffee.setId(coffeeId);
        user.getFavoriteCoffees().add(coffee);  // Кофе уже в избранном

        // Мокаем поведение репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(coffee));

        // Вызов метода
        userService.removeCoffeeFromFavorites(userId, coffeeId);

        // Проверка, что кофе удалено
        assertFalse(user.getFavoriteCoffees().contains(coffee), "Кофе должно быть удалено из избранного");
    }

    @Test
    void testRemoveCoffeeFromFavorites_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        Long coffeeId = 1L;

        // Мокаем поведение репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Проверка на исключение
        assertThrows(IllegalArgumentException.class, () -> userService.removeCoffeeFromFavorites(userId, coffeeId),
                "Метод должен выбросить исключение, если пользователь не найден");
    }

    @Test
    void testGetAllUsers_ShouldReturnListOfUserDto() {
        // Подготовка данных
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");

        // Мокаем поведение репозитория
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Вызов метода
        List<UserDto> result = userService.getAllUsers();

        // Проверка результатов
        assertNotNull(result, "Результат не должен быть null");
        assertEquals(1, result.size(), "Размер списка должен быть 1");
        assertEquals("John Doe", result.get(0).getName(), "Имя должно быть John Doe");
    }

    @Test
    void testGetUserById_ShouldReturnUserDto() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("John Doe");

        // Мокаем поведение репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Вызов метода
        Optional<UserDto> result = userService.getUserById(userId);

        // Проверка результатов
        assertTrue(result.isPresent(), "Результат должен быть present");
        assertEquals("John Doe", result.get().getName(), "Имя должно быть John Doe");
    }

    @Test
    void testGetUserById_ShouldReturnEmpty_WhenUserNotFound() {
        Long userId = 1L;

        // Мокаем поведение репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Вызов метода
        Optional<UserDto> result = userService.getUserById(userId);

        // Проверка результатов
        assertFalse(result.isPresent(), "Результат должен быть empty");
    }

}
