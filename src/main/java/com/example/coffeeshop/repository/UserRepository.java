package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Repos. */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Вариант 1: Используем @EntityGraph
    @EntityGraph(attributePaths = {"favoriteCoffees", "orders", "orders.coffees"})
    Optional<User> findWithRelationsById(Long id);
}
