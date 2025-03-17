package com.example.coffeeshop.dto;

import java.util.List;
import lombok.Data;

/** Order Dto. */
@Data
public class CreateOrderDto {
    private Long id;
    private Long userId;
    private List<Long> coffeesIds;
    private String notes;
}
