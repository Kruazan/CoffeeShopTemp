package com.example.coffeeshop.dto;

import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.model.User;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private User user;
    private List<Coffee> coffees;
    private String notes;
}
