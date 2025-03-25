package com.example.coffeeshop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/** Order Dto. */
@Data
public class CreateOrderDto {
    private Long id;

    @NotNull(message = "ID пользователя не может быть пустым")
    private Long userId;

    @NotEmpty(message = "Список кофе не может быть пустым")
    private List<Long> coffeesIds;

    private String notes;
}