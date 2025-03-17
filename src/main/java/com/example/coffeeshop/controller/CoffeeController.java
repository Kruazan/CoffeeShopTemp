package com.example.coffeeshop.controller;

import com.example.coffeeshop.dto.CoffeeDto;
import com.example.coffeeshop.service.CoffeeService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Coffee controller. */
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
    @GetMapping
    public List<CoffeeDto> getAllCoffees() {
        return coffeeService.getAllCoffees();
    }

    /** Get coffee by id. */
    @GetMapping("/{id}")
    public ResponseEntity<CoffeeDto> getCoffeeById(@PathVariable Long id) {
        Optional<CoffeeDto> coffee = coffeeService.getCoffeeById(id);
        return coffee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /** Create coffee. */
    @PostMapping
    public ResponseEntity<CoffeeDto> createCoffee(@RequestBody CoffeeDto coffeeDto) {
        CoffeeDto createdCoffee = coffeeService.createCoffee(coffeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoffee);
    }

    /** Update coffee. */
    @PatchMapping("/{id}")
    public ResponseEntity<CoffeeDto> updateCoffee(@PathVariable Long id, @RequestBody CoffeeDto coffeeDto) {
        CoffeeDto updatedCoffee = coffeeService.updateCoffee(id, coffeeDto);
        return updatedCoffee != null ? ResponseEntity.ok(updatedCoffee) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /** Delete coffee. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoffee(@PathVariable Long id) {
        return coffeeService.deleteCoffee(id) ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
