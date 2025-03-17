package com.example.coffeeshop.service;

import com.example.coffeeshop.dto.UserDto;
import com.example.coffeeshop.mapper.UserMapper;
import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.model.User;
import com.example.coffeeshop.repository.CoffeeRepository;
import com.example.coffeeshop.repository.UserRepository;
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
    public UserDto createUser(UserDto userDto) throws NoSuchAlgorithmException {
        User user = userMapper.toEntity(userDto);
        String hashedPassword = hashPassword(userDto.getPasswordHash());
        user.setPassword(hashedPassword);
        return userMapper.toDto(userRepository.save(user));
    }

    /** Update user. */
    public UserDto updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userDto.getPhoneNumber() != null) {
                        user.setPhoneNumber(userDto.getPhoneNumber());
                    }
                    if (userDto.getName() != null) {
                        user.setName(userDto.getName());
                    }
                    if (userDto.getPasswordHash() != null) {
                        user.setPassword(userDto.getPasswordHash());
                    }
                    return userMapper.toDto(userRepository.save(user));
                })
                .orElse(null); // Либо кинь исключение, если юзера нет
    }


    /** Delete user. */
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
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // используем SHA-256
        byte[] hashBytes = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b)); // преобразуем байты в строку в шестнадцатеричном формате
        }
        return hexString.toString();
    }

}
