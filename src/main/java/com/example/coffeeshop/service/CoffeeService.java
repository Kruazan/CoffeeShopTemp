package com.example.coffeeshop.service;

import com.example.coffeeshop.dto.CoffeeDto;
import com.example.coffeeshop.mapper.CoffeeMapper;
import com.example.coffeeshop.model.Coffee;
import com.example.coffeeshop.repository.CoffeeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service. */
@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final CoffeeMapper coffeeMapper;

    /** Constructor. */
    @Autowired
    public CoffeeService(CoffeeRepository coffeeRepository, CoffeeMapper coffeeMapper) {
        this.coffeeRepository = coffeeRepository;
        this.coffeeMapper = coffeeMapper;
    }

    /** Get all coffees. */
    public List<CoffeeDto> getAllCoffees() {
        return coffeeRepository.findAll().stream()
                .map(coffeeMapper::toDto)
                .toList();
    }

    /** Get coffee by id. */
    public Optional<CoffeeDto> getCoffeeById(Long id) {
        return coffeeRepository.findById(id).map(coffeeMapper::toDto);
    }

    /** Create coffee. */
    public CoffeeDto createCoffee(CoffeeDto coffeeDto) {
        // Проверка на существование кофе с таким названием
        Optional<Coffee> existingCoffee = coffeeRepository.findByName(coffeeDto.getName());
        if (existingCoffee.isPresent()) {
            throw new IllegalArgumentException("Кофе с таким названием уже существует");
        }

        Coffee coffee = coffeeMapper.toEntity(coffeeDto);
        return coffeeMapper.toDto(coffeeRepository.save(coffee));
    }

    /** Update coffee info. */
    public CoffeeDto updateCoffee(Long id, CoffeeDto coffeeDto) {
        return coffeeRepository.findById(id)
                .map(coffee -> {
                    if (coffeeDto.getName() != null) {
                        coffee.setName(coffeeDto.getName());
                    }
                    if (coffeeDto.getType() != null) {
                        coffee.setType(coffeeDto.getType());
                    }
                    if (coffeeDto.getPrice() != 0) {
                        coffee.setPrice(coffeeDto.getPrice());
                    }
                    return coffeeMapper.toDto(coffeeRepository.save(coffee));
                })
                .orElse(null); // Можно заменить на выброс исключения, если кофе не найден
    }

    /** Delete coffee. */
    public boolean deleteCoffee(Long id) {
        if (coffeeRepository.existsById(id)) {
            coffeeRepository.deleteById(id);
            return true;
        }
        return false; // Кофе с таким ID не найдено
    }
}
