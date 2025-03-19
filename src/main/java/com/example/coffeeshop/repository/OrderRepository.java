package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Order;
import java.util.List;
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

    /** @Query(value = "SELECT o.* FROM orders o JOIN users u ON o.user_id = u.id WHERE u.phone_number = :phoneNumber", nativeQuery = true)
        List<Order> findAllByUserPhoneNumberNative(@Param("phoneNumber") String phoneNumber); */

}
