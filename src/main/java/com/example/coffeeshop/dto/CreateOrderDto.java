package com.example.coffeeshop.dto;

import java.util.List;
import lombok.Data;

@Data
public class CreateOrderDto {
    private Long id;
    private Long userId;
    private List<Long> coffeesIds;
    private String notes;
}
