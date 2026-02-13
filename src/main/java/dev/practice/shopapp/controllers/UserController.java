package dev.practice.shopapp.controllers;

import dev.practice.shopapp.SortingOptions;
import dev.practice.shopapp.models.ApiResponse;
import dev.practice.shopapp.models.User;
import dev.practice.shopapp.services.UserServiceImpl;
import dev.practice.shopapp.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserServiceImpl userService = new UserServiceImpl();

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user,
                                                        HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.createUser(user),
                        "User successfully created",
                        request.getRequestURI()),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(
            HttpServletRequest request,
            @RequestParam(defaultValue = "ID_ASC", required = false) Optional<SortingOptions> sortBy ) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.getAllUsers(sortBy),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long userId,
                                                         HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.getUserById(userId),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long userId,
                                                        @Valid @RequestBody User user,
                                                        HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.updateUser(userId, user),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId,
                                                          HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.deleteUser(userId),"Success",
                request.getRequestURI()), HttpStatus.OK);
    }
}
