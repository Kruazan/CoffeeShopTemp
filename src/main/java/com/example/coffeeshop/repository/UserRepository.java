package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repos. */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
