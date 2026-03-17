package dev.practice.shopapp.controllers;

import dev.practice.shopapp.dto.OrderCreateRequest;
import dev.practice.shopapp.dto.OrderResponse;
import dev.practice.shopapp.dto.OrderStatusUpdateRequest;
import dev.practice.shopapp.models.ApiResponse;
import dev.practice.shopapp.services.OrderService;
import dev.practice.shopapp.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 🚀 CREATE: Processes the checkout and returns a wrapped success response
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderCreateRequest dto,
            HttpServletRequest request) {

        OrderResponse response = orderService.createOrder(dto);
        return new ResponseEntity<>(
                ResponseUtil.success(response, "Order created successfully", request.getRequestURI()),
                HttpStatus.CREATED
        );
    }

    // 🚀 MY ORDERS: Sarah retrieves her own historical transaction logs
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(HttpServletRequest request) {

        List<OrderResponse> orders = orderService.getOrdersForCurrentUser();
        return new ResponseEntity<>(
                ResponseUtil.success(orders, "Personal order history retrieved", request.getRequestURI()),
                HttpStatus.OK
        );
    }

    // 🚀 ALL ORDERS: Alex (Admin) monitors the entire fleet's transaction stream
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders(HttpServletRequest request) {

        List<OrderResponse> orders = orderService.getAllOrders();
        return new ResponseEntity<>(
                ResponseUtil.success(orders, "Global order registry retrieved", request.getRequestURI()),
                HttpStatus.OK
        );
    }

    // 🚀 UPDATE STATUS: Admin manages the lifecycle of an existing order
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest dto,
            HttpServletRequest request) {

        OrderResponse response = orderService.updateOrderStatus(id, dto);
        return new ResponseEntity<>(
                ResponseUtil.success(response, "Order status updated successfully", request.getRequestURI()),
                HttpStatus.OK
        );
    }
}

