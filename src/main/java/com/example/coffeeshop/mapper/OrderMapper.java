package com.example.coffeeshop.mapper;

import com.example.coffeeshop.dto.DisplayOrderDto;
import com.example.coffeeshop.dto.OrderDto;
import com.example.coffeeshop.model.Order;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final UserMapper userMapper;

    public OrderMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUser(order.getUser());
        dto.setCoffees(order.getCoffees());
        dto.setNotes(order.getNotes());
        return dto;
    }

    public List<OrderDto> toDto(List<Order> orders) {
        return orders.stream()
                .map(this::toDto)  // Преобразуем каждый заказ в DTO
                .toList(); // Собираем в список
    }

    public DisplayOrderDto toDisplayDto(Order order) {
        DisplayOrderDto dto = new DisplayOrderDto();
        dto.setId(order.getId());
        dto.setUser(userMapper.toDisplayUserDto(order.getUser()));
        dto.setCoffees(order.getCoffees());
        dto.setNotes(order.getNotes());
        return dto;
    }

    public List<DisplayOrderDto> toDisplayDto(List<Order> orders) {
        return orders.stream()
                .map(this::toDisplayDto)
                .toList();
    }
}
