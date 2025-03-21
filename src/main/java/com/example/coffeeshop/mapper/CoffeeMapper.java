package com.example.coffeeshop.mapper;

import com.example.coffeeshop.dto.CoffeeDto;
import com.example.coffeeshop.model.Coffee;
import java.util.List;
import org.springframework.stereotype.Component;

/** Coffee mapper. */
@Component
public class CoffeeMapper {

    /** To dto. */
    public CoffeeDto toDto(Coffee coffee) {
        CoffeeDto dto = new CoffeeDto();
        dto.setId(coffee.getId());
        dto.setName(coffee.getName());
        dto.setType(coffee.getType());
        dto.setPrice(coffee.getPrice());
        return dto;
    }

    /** To dto. */
    public List<CoffeeDto> toDto(List<Coffee> coffees) {
        return coffees.stream()
                .map(this::toDto)
                .toList();
    }

    /** To entity. */
    public Coffee toEntity(CoffeeDto dto) {
        Coffee coffee = new Coffee();
        coffee.setId(dto.getId());
        coffee.setName(dto.getName());
        coffee.setType(dto.getType());
        coffee.setPrice(dto.getPrice());
        return coffee;
    }
}
