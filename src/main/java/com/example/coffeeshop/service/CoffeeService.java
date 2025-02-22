package com.example.coffeeshop.service;

import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.repository.CoffeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;

    public CoffeeService(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    public List<Coffee> getAllCoffees() {
        return coffeeRepository.findAll();
    }

    public Coffee getCoffeeById(Long id) {
        return coffeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Coffee not found with id: " + id));
    }

    public Coffee createCoffee(Coffee coffee) {
        return coffeeRepository.save(coffee);
    }

    public Coffee updateCoffee(Long id, Coffee coffee) {
        Coffee existingCoffee = getCoffeeById(id);
        existingCoffee.setName(coffee.getName());
        existingCoffee.setType(coffee.getType());
        existingCoffee.setPrice(coffee.getPrice());
        existingCoffee.setIngredients(coffee.getIngredients());
        existingCoffee.setSize(coffee.getSize());
        existingCoffee.setDescription(coffee.getDescription());
        existingCoffee.setCalories(coffee.getCalories());
        existingCoffee.setImageUrl(coffee.getImageUrl());
        existingCoffee.setAvailability(coffee.getAvailability());
        return coffeeRepository.save(existingCoffee);
    }

    public void deleteCoffee(Long id) {
        coffeeRepository.deleteById(id);
    }
}
