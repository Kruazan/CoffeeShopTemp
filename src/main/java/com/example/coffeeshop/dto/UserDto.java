package com.example.coffeeshop.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

/** User Dto. */
@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "Номер телефона не может быть пустым")
    private String phoneNumber;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Хеш пароля не может быть пустым")
    private String passwordHash;

    private List<CoffeeDto> orders;
    private List<CoffeeDto> favorites;
}