package com.example.coffeeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeWithOrdersDto {
    private Long id;
    private String name;
    private String type;
    private Double price;
    private List<OrderInfoDto> orders;
}