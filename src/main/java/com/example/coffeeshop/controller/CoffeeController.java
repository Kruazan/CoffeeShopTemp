package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Coffee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/** Controller. */
@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    /** Path endpoint. */
    @GetMapping("/{id}")
    public Coffee getCoffeeById(@PathVariable Long id) {
        return new Coffee(id, "Latte", "Milk-based", 4.5);
    }

    /** Query endpoint. */
    @GetMapping("/info")
    public Coffee getCoffeeByTypeAndPrice(@RequestParam String type, @RequestParam double price) {
        return new Coffee(2L, "Cappuccino", type, price);
    }
}