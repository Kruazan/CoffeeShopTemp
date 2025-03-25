package com.example.coffeeshop.controller;

import com.example.coffeeshop.dto.CreateOrderDto;
import com.example.coffeeshop.dto.DisplayOrderDto;
import com.example.coffeeshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Order controller. */
@Tag(name = "Orders", description = "Управление заказами")
@Validated
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    /** Constructor. */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** Create order. */
    @Operation(summary = "Создать заказ", description = "Создаёт новый заказ на основе переданных данных")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Заказ успешно создан"),
        @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных")
    })
    @PostMapping
    public ResponseEntity<DisplayOrderDto> createOrder(@Valid @RequestBody CreateOrderDto createOrderDto) {
        DisplayOrderDto createdOrder = orderService.createOrder(createOrderDto);
        if (createdOrder != null) {
            return ResponseEntity.ok(createdOrder);
        }
        return ResponseEntity.badRequest().body(null);
    }

    /** Delete order. */
    @Operation(summary = "Удалить заказ", description = "Удаляет заказ по его ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Заказ успешно удалён"),
        @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.deleteOrder(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /** Get order by id. */
    @Operation(summary = "Получить заказ по ID", description = "Возвращает данные заказа по его ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Заказ найден"),
        @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DisplayOrderDto> getOrderById(@PathVariable @Min(1) Long id) {
        DisplayOrderDto displayOrderDto = orderService.getOrderById(id);
        if (displayOrderDto != null) {
            return ResponseEntity.ok(displayOrderDto);
        }
        return ResponseEntity.notFound().build();
    }

    /** Get order by user id. */
    @Operation(summary = "Получить заказы пользователя", description = "Возвращает список заказов по ID пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Заказы найдены")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DisplayOrderDto>> getOrdersByUserId(@PathVariable @Min(1) Long userId) {
        List<DisplayOrderDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /** Get all orders by user phone number. */
    @Operation(summary = "Фильтр заказов по номеру телефона", description = "Возвращает список заказов по номеру телефона пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Заказы найдены"),
        @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных")
    })
    @GetMapping("/filter")
    public ResponseEntity<List<DisplayOrderDto>> getOrdersByPhoneNumber(@RequestParam @NotBlank(message = "не должно быть пустым")
                                                                            String phoneNumber) {
        List<DisplayOrderDto> orders = orderService.filterCarsByBrand(phoneNumber);
        return ResponseEntity.ok(orders);
    }
}
