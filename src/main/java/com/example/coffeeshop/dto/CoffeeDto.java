package com.example.coffeeshop.dto;

import lombok.Data;

/** Coffee Dto. */
@Data
public class CoffeeDto {
    private Long id;
    private String name;
    private String type;
    private double price;
}
