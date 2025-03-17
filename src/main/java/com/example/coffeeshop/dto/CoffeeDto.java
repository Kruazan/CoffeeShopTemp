package com.example.coffeeshop.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CoffeeDto {
    private Long id;
    private String name;
    private String type;
    private double price;
}
