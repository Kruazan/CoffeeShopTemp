package com.example.coffeeshop.dto;

import java.util.List;
import lombok.Data;

/** Dto. */
@Data
public class UserDto {
    private Long id;
    private String phoneNumber;
    private String name;
    private String passwordHash;
    private List<CoffeeDto> orders;  // Список ID заказов
    private List<CoffeeDto> favorites;  // Список ID любимых кофе
}
