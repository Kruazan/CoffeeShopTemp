package com.example.coffeeshop.controller;

import com.example.coffeeshop.dto.UserDto;
import com.example.coffeeshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller. */
@Tag(name = "Users", description = "Управление пользователями")
@Validated
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
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей в системе")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    /** Get by id. */
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по указанному ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable @Min(1) Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /** Crate user. */
    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя на основе переданных данных")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /** Update user. */
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя по указанному ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /** Delete user. */
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по его ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Min(1) Long id) {
        return userService.deleteUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /** Get favorites. */
    @Operation(summary = "Получить избранное пользователя", description = "Возвращает список избранных товаров пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Список избранных товаров успешно получен")
    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<Long>> getFavoriteUsers(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(userService.getAllFavoritesCoffees(id));
    }

    /** Add to fav. */
    @Operation(summary = "Добавить кофе в избранное", description = "Добавляет кофе в избранное пользователя по его ID")
    @ApiResponse(responseCode = "200", description = "Кофе успешно добавлено в избранное")
    @PostMapping("/{id}/favorites/{coffeeId}")
    public ResponseEntity<Void> addCoffeeToFavorites(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long coffeeId) {
        userService.addCoffeeToFavorites(id, coffeeId);
        return ResponseEntity.ok().build();
    }

    /** Delete from fav. */
    @Operation(summary = "Удалить кофе из избранного", description = "Удаляет кофе из избранного пользователя по его ID")
    @ApiResponse(responseCode = "204", description = "Кофе успешно удалено из избранного")
    @DeleteMapping("/{id}/favorites/{coffeeId}")
    public ResponseEntity<Void> removeCoffeeFromFavorites(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long coffeeId) {
        userService.removeCoffeeFromFavorites(id, coffeeId);
        return ResponseEntity.noContent().build();
    }

}
