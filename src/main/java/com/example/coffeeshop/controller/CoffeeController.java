package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Coffee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Controller. */
@RestController
public class CoffeeController {
    /** Params processing . */
    @GetMapping("/coffees")
    public Coffee getCoffees(
            @RequestParam(required = false, defaultValue = "all") String type,
            @RequestParam(required = false, defaultValue = "medium") String size
    ) {
        return new Coffee(null, type, size);
    }

    /** Processing path params. */
    @GetMapping("/coffees/{id}")
    public Coffee getCoffee(
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "espresso") String type,
            @RequestParam(required = false, defaultValue = "medium") String size
    ) {
        return new Coffee(id, type, size);
    }
}