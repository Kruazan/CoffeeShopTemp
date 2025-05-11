package com.example.coffeeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeInfoDto {
    private Long id;
    private String name;
    private String type;
    private Double price;
}