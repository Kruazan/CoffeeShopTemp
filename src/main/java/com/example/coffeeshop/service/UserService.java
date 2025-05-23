package com.example.coffeeshop.service;

import com.example.coffeeshop.dto.*;
import com.example.coffeeshop.exception.PasswordHashingException;
import com.example.coffeeshop.exception.UserUpdateException;
import com.example.coffeeshop.mapper.UserMapper;
import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.model.User;
import com.example.coffeeshop.repository.CoffeeRepository;
import com.example.coffeeshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service. */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CoffeeRepository coffeeRepository;

    /** Constructor. */
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, CoffeeRepository coffeeRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.coffeeRepository = coffeeRepository;
    }

    /** Get all users. */
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    /** Get by id. */
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    /** Create User. */
    @Transactional
    public UserDto createUser(UserDto userDto) throws NoSuchAlgorithmException {
        // Проверка на пустое имя или номер телефона
        if (userDto.getPhoneNumber() == null || userDto.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Номер телефона не может быть пустым.");
        }
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }

        // Хеширование пароля
        String hashedPassword = hashPassword(userDto.getPasswordHash());

        // Преобразование из DTO в Entity
        User user = userMapper.toEntity(userDto);
        user.setPassword(hashedPassword);

        // Сохранение пользователя в БД
        User savedUser = userRepository.save(user);

        // Возвращение DTO с данными созданного пользователя
        return userMapper.toDto(savedUser);
    }


    /** Update user. */
    @Transactional
    public UserDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userUpdateDto.getPhoneNumber() != null) {
                        if (userUpdateDto.getPhoneNumber().isBlank()) {
                            throw new UserUpdateException("Номер телефона не может быть пустым.");
                        }
                        user.setPhoneNumber(userUpdateDto.getPhoneNumber());
                    }
                    if (userUpdateDto.getName() != null) {
                        if (userUpdateDto.getName().isBlank()) {
                            throw new UserUpdateException("Имя не может быть пустым.");
                        }
                        user.setName(userUpdateDto.getName());
                    }
                    if (userUpdateDto.getPasswordHash() != null) {
                        try {
                            user.setPassword(hashPassword(userUpdateDto.getPasswordHash()));
                        } catch (NoSuchAlgorithmException e) {
                            throw new PasswordHashingException("Ошибка хеширования пароля", e);
                        }
                    }
                    return userMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(() -> new UserUpdateException("Пользователь с ID " + id + " не найден."));
    }

    /** Delete user. */
    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false; // Пользователь с таким ID не найден
    }

    /** Get all fav. */
    public List<Long> getAllFavoritesCoffees(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getFavoriteCoffees().stream().map(Coffee::getId).toList())
                .orElse(List.of());
    }

    /** Add to fav. */
    @Transactional
    public void addCoffeeToFavorites(Long userId, Long coffeeId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Coffee> coffeeOpt = coffeeRepository.findById(coffeeId);

        if (userOpt.isEmpty() || coffeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Пользователь или кофе не найдены.");
        }

        User user = userOpt.get();
        Coffee coffee = coffeeOpt.get();

        if (!user.getFavoriteCoffees().contains(coffee)) {
            user.getFavoriteCoffees().add(coffee);
            userRepository.save(user);
        }
    }

    /** Delete from fav. */
    @Transactional
    public void removeCoffeeFromFavorites(Long userId, Long coffeeId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Coffee> coffeeOpt = coffeeRepository.findById(coffeeId);

        if (userOpt.isEmpty() || coffeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Пользователь или кофе не найдены.");
        }

        User user = userOpt.get();
        Coffee coffee = coffeeOpt.get();

        if (user.getFavoriteCoffees().contains(coffee)) {
            user.getFavoriteCoffees().remove(coffee);
            userRepository.save(user);
        }
    }

    /** Hash password. */
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // используем SHA-256
        byte[] hashBytes = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b)); // преобразуем байты в строку в шестнадцатеричном формате
        }
        return hexString.toString();
    }


    @Transactional(readOnly = true)
    public UserWithRelationsDto getUserWithRelations(Long id) {
        User user = userRepository.findWithRelationsById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        return new UserWithRelationsDto(
                user.getId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getFavoriteCoffees().stream()
                        .map(coffee -> new CoffeeInfoDto(
                                coffee.getId(),
                                coffee.getName(),
                                coffee.getType(),
                                coffee.getPrice()
                        ))
                        .toList(),
                user.getOrders().stream()
                        .map(order -> new OrderInfoDto(
                                order.getId(),
                                order.getUser().getName(),
                                order.getNotes()
                        ))
                        .toList()
        );
    }
}
