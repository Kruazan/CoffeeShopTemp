package com.example.coffeeshop.dto;

import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.model.User;
import java.util.List;
import lombok.Data;

/** Oder Dto. */
@Data
public class OrderDto {
    private Long id;
    private User user;
    private List<Coffee> coffees;
    private String notes;
}
