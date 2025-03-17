package com.example.coffeeshop.dto;

import com.example.coffeeshop.model.Coffee;
import java.util.List;
import lombok.Data;

/** Order Dto. */
@Data
public class DisplayOrderDto {
    private Long id;
    private DisplayUserDto user;
    private List<Coffee> coffees;
    private String notes;
}
