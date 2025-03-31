package com.example.coffeeshop.dto;

import lombok.Data;

/** Dto для обновления данных пользователя. */
@Data
public class UserUpdateDto {
    private String phoneNumber;
    private String name;
    private String passwordHash;
}
