package com.example.coffeeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithRelationsDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private List<CoffeeInfoDto> favoriteCoffees;
    private List<OrderInfoDto> orders;
}