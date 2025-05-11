package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Order;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Repos. */
public interface OrderRepository extends JpaRepository<Order, Long> {
    /** Find by user id. */
    List<Order> findByUserId(Long userId); // Получение всех заказов пользователя

    /** JPQL. */
    @Query("SELECT o FROM Order o WHERE o.user.phoneNumber = :phoneNumber")
    List<Order> findAllByUserPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @EntityGraph(attributePaths = {"coffees", "user"})
    Optional<Order> findWithDetailsById(Long id);

    @Query("SELECT o FROM Order o JOIN o.coffees c WHERE c.id = :coffeeId")
    List<Order> findByCoffeeId(@Param("coffeeId") Long coffeeId);
}
