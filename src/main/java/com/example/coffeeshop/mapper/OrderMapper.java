package com.example.coffeeshop.mapper;

import com.example.coffeeshop.dto.DisplayOrderDto;
import com.example.coffeeshop.model.Order;
import java.util.List;
import org.springframework.stereotype.Component;

/** Mapper. */
@Component
public class OrderMapper {

    private final UserMapper userMapper;

    /** Constructor. */
    public OrderMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /** To dto also. */
    public DisplayOrderDto toDisplayDto(Order order) {
        DisplayOrderDto dto = new DisplayOrderDto();
        dto.setId(order.getId());
        dto.setUser(userMapper.toDisplayUserDto(order.getUser()));
        dto.setCoffees(order.getCoffees());
        dto.setNotes(order.getNotes());
        return dto;
    }

    /** To dto also yeah. */
    public List<DisplayOrderDto> toDisplayDto(List<Order> orders) {
        return orders.stream()
                .map(this::toDisplayDto)
                .toList();
    }
}
