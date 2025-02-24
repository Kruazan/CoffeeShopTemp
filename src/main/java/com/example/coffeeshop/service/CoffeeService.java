package com.example.coffeeshop.service;

import com.example.coffeeshop.model.Coffee;
import org.springframework.stereotype.Service;

/** Service. */
@Service
public class CoffeeService {

    /** Get method. */
    public Coffee getCoffeeById(Long id) {
        return new Coffee(id, "Latte", "Milk-based", 4.5);
    }

    /** Get method. */
    public Coffee getCoffeeByTypeAndPrice(String type, double price) {
        return new Coffee(2L, "Cappuccino", type, price);
    }
}
