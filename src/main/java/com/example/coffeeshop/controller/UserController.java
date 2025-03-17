package com.example.coffeeshop.controller;

import com.example.coffeeshop.dto.UserDto;
import com.example.coffeeshop.service.UserService;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller. */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /** Constructor. */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Get all users. */
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    /** Get by id. */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /** Crate user. */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /** Update user. */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /** Delete user. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /** Get favorites. */
    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<Long>> getFavoriteUsers(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAllFavoritesCoffees(id));
    }

    /** Add to fav. */
    @PostMapping("/{id}/favorites/{coffeeId}")
    public  ResponseEntity<Void> addCoffeeToFavorites(@PathVariable Long id, @PathVariable Long coffeeId) {
        userService.addCoffeeToFavorites(id, coffeeId);
        return ResponseEntity.ok().build();
    }

    /** Delete from fav. */
    @DeleteMapping("/{id}/favorites/{coffeeId}")
    public ResponseEntity<Void> removeCoffeeFromFavorites(@PathVariable Long id, @PathVariable Long coffeeId) {
        userService.removeCoffeeFromFavorites(id, coffeeId);
        return ResponseEntity.noContent().build();
    }

}
