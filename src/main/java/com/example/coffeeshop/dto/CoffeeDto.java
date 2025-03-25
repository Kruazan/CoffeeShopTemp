package com.example.coffeeshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Coffee Dto. */
@Data
public class CoffeeDto {
    private Long id;

    @NotBlank(message = "Название кофе не может быть пустым")
    private String name;

    @NotBlank(message = "Тип кофе не может быть пустым")
    private String type;

    @Min(value = 0, message = "Цена должна быть положительной")
    private double price;
}