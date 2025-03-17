package com.example.coffeeshop.mapper;

import com.example.coffeeshop.dto.DisplayUserDto;
import com.example.coffeeshop.dto.UserDto;
import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.model.User;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setName(user.getName());
        dto.setPasswordHash(user.getPassword());

        // Список ID заказов
        List<Coffee> coffees = user.getOrders() != null ? user.getOrders().stream()
                .flatMap(order -> order.getCoffees().stream()).toList() : null;
        dto.setOrders(coffees);

        // Список ID любимых кофе
        List<Coffee> favoritesCoffees = user.getFavoriteCoffees() != null ? user.getFavoriteCoffees()
                .stream().toList() : null;
        dto.setFavorites(favoritesCoffees);
        return dto;
    }

    public DisplayUserDto toDisplayUserDto(User user) {
        DisplayUserDto dto = new DisplayUserDto();
        dto.setId(user.getId());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setName(user.getName());
        return dto;
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setName(dto.getName());
        user.setPassword(dto.getPasswordHash());
        // Преобразование из ID в объекты нужно будет реализовать, если это нужно в вашем случае.
        return user;
    }
}
