package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.service.CoffeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Controller. */
@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    private final CoffeeService coffeeService;

    /** Coffee controller. */
    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    /** Path. */
    @GetMapping("/{id}")
    public Coffee getCoffeeById(@PathVariable Long id) {
        return coffeeService.getCoffeeById(id);
    }

    /** Query. */
    @GetMapping("/info")
    public Coffee getCoffeeByTypeAndPrice(@RequestParam String type, @RequestParam double price) {
        return coffeeService.getCoffeeByTypeAndPrice(type, price);
    }
}
