package com.example.coffeeshop.dto;

import com.example.coffeeshop.model.Coffee;
import java.util.List;
import lombok.Data;

/** Dto. */
@Data
public class UserDto {
    private Long id;
    private String phoneNumber;
    private String name;
    private String passwordHash;
    private List<Coffee> orders;  // Список ID заказов
    private List<Coffee> favorites;  // Список ID любимых кофе
}
