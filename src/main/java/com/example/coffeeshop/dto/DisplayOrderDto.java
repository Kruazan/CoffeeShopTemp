package com.example.coffeeshop.dto;


import java.util.List;
import lombok.Data;

/** Order Dto. */
@Data
public class DisplayOrderDto {
    private Long id;
    private DisplayUserDto user;
    private List<CoffeeDto> coffees;
    private String notes;
}
