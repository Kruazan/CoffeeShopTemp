package com.example.coffeeshop.controller;

import com.example.coffeeshop.dto.CoffeeDto;
import com.example.coffeeshop.dto.CoffeeUpdateDto;
import com.example.coffeeshop.service.CoffeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Coffee controller. */
@Tag(name = "Кофе", description = "Управление кофе")
@Validated
@RestController
@RequestMapping("/coffees")
public class CoffeeController {

    private final CoffeeService coffeeService;

    /** Constructor. */
    @Autowired
    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    /** Get all coffees. */
    @Operation(summary = "Получить список всех кофе")
    @GetMapping
    public List<CoffeeDto> getAllCoffees() {
        return coffeeService.getAllCoffees();
    }

    /** Get coffee by id. */
    @Operation(summary = "Получить кофе по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CoffeeDto> getCoffeeById(
            @Parameter(description = "ID кофе") @PathVariable @Min(1) Long id) {
        Optional<CoffeeDto> coffee = coffeeService.getCoffeeById(id);
        return coffee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /** Create coffee. */
    @Operation(summary = "Создать новый кофе")
    @PostMapping
    public ResponseEntity<CoffeeDto> createCoffee(@Valid @RequestBody CoffeeDto coffeeDto) {
        CoffeeDto createdCoffee = coffeeService.createCoffee(coffeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoffee);
    }

    /**Da. */
    @PatchMapping("/{id}")
    public ResponseEntity<CoffeeDto> updateCoffee(
            @Parameter(description = "ID кофе") @PathVariable Long id,
            @RequestBody CoffeeUpdateDto coffeeDto) {
        CoffeeDto updatedCoffee = coffeeService.updateCoffee(id, coffeeDto);
        return ResponseEntity.ok(updatedCoffee);
    }


    /** Delete coffee. */
    @Operation(summary = "Удалить кофе по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoffee(
            @Parameter(description = "ID кофе") @PathVariable @Min(1) Long id) {
        return coffeeService.deleteCoffee(id) ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**Bulk operation.*/
    @Operation(summary = "Создать несколько новых кофе")
    @PostMapping("/bulk")
    public ResponseEntity<List<CoffeeDto>> createCoffeesBulk(@Valid @RequestBody List<CoffeeDto> coffeeDtos) {
        // Используем Stream API и лямбда-выражения для обработки списка
        @SuppressWarnings("all")
        List<CoffeeDto> createdCoffees = coffeeDtos.stream()
                .map(coffeeDto -> coffeeService.createCoffee(coffeeDto))  // Создаем кофе для каждого объекта
                .toList();  // Собираем результат в новый список

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoffees);
    }
}
