package com.example.coffeeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithDetailsDto {
    private Long id;
    private String user;
    private String notes;
    private List<CoffeeInfoDto> coffees;
}