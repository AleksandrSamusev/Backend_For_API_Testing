package dev.practice.shopapp.services;

import dev.practice.shopapp.dto.OrderCreateRequest;
import dev.practice.shopapp.dto.OrderResponse;
import dev.practice.shopapp.dto.OrderStatusUpdateRequest;
import java.util.List;

public interface OrderService {

    // 🚀 CREATE: Processes the checkout and locks in the transaction
    OrderResponse createOrder(OrderCreateRequest request);

    // 🚀 RETRIEVE: Sarah views her own historical mission logs
    List<OrderResponse> getOrdersForCurrentUser();

    // 🚀 ADMINISTRATIVE: Alex monitors the entire fleet's orders
    List<OrderResponse> getAllOrders();

    // 🚀 UPDATE: Alex manages the order lifecycle (e.g., PAID to SHIPPED)
    OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request);
}