package dev.practice.shopapp.controllers;

import dev.practice.shopapp.dto.AddressDto;
import dev.practice.shopapp.enums.SortingOptions;
import dev.practice.shopapp.dto.UserCreateDto;
import dev.practice.shopapp.dto.UserUpdateDto;
import dev.practice.shopapp.models.ApiResponse;
import dev.practice.shopapp.models.User;
import dev.practice.shopapp.services.UserService;
import dev.practice.shopapp.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Operations for onboarding, updating, and managing system users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Validates input and persists a new user to the database."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody UserCreateDto dto,
                                                        HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.createUser(dto),
                        "User successfully created",
                        request.getRequestURI()),
                HttpStatus.CREATED);
    }

    @Operation(
            summary = "Search all users",
            description = "Retrieves a list of all registered users with optional sorting criteria."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(
            HttpServletRequest request,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ID_ASC", required = false) SortingOptions sortBy) {

        Page<User> userPage = userService.getAllUsers(search, page, size, sortBy);

        return new ResponseEntity<>(ResponseUtil.success(userPage, "Success", request.getRequestURI()), HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = User.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserById(
            @Parameter(description = "The database ID of the user", example = "1")
            @PathVariable Long userId,
            HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.getUserById(userId),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK);
    }

    @Operation(summary = "Update existing user", description = "Updates profile information. Partial updates are supported depending on DTO structure.")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @Parameter(description = "The database ID of the user", example = "1")
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDto dto,
            HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.updateUser(userId, dto),
                        "Success",
                        request.getRequestURI()), HttpStatus.OK);
    }

    @Operation(summary = "Delete user account", description = "Permanently removes the user from the system. This action cannot be undone.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @Parameter(description = "The database ID of the user", example = "1")
            @PathVariable Long userId,
            HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.deleteUser(userId), "Success",
                        request.getRequestURI()), HttpStatus.OK);
    }

    // ADDRESSES HANDLING

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<ApiResponse<User>> addAddress(
            @PathVariable Long userId,
            @Valid @RequestBody AddressDto dto,
            HttpServletRequest request) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.addAddress(userId, dto),
                        "Address added successfully",
                        request.getRequestURI()), HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<ApiResponse<User>> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                ResponseUtil.success(userService.removeAddress(userId, addressId),
                        "Address removed successfully",
                        request.getRequestURI()), HttpStatus.OK
        );
    }

    @PatchMapping("/{userId}/addresses/{addressId}/promote")
    public ResponseEntity<ApiResponse<User>> promoteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            HttpServletRequest request) {

        User updatedUser = userService.promoteAddress(userId, addressId);

        return new ResponseEntity<>(
                ResponseUtil.success(
                        updatedUser,
                        "Address promoted to primary successfully",
                        request.getRequestURI()
                ),
                HttpStatus.OK
        );
    }
}
