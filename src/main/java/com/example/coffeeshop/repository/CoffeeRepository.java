package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Coffee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repos. */
@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
    /** Find by name. */
    Optional<Coffee> findByName(String name);
}
