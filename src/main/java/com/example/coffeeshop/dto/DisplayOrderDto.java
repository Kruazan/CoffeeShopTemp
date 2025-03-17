package com.example.coffeeshop.dto;

import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.model.User;
import lombok.Data;

import java.util.List;
@Data

public class DisplayOrderDto {
    private Long id;
    private DisplayUserDto user;
    private List<Coffee> coffees;
    private String notes;
}
