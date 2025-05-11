package com.example.coffeeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoDto {
    private Long id;
    private String user;
    private String notes;
}