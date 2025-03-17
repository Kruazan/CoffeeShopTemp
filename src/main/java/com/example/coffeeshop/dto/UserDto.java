package com.example.coffeeshop.dto;

import com.example.coffeeshop.model.Coffee;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String phoneNumber;
    private String name;
    private String passwordHash;
    private List<Coffee> orders;  // Список ID заказов
    private List<Coffee> favorites;  // Список ID любимых кофе
}
