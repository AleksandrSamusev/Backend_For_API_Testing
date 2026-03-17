package dev.practice.shopapp.services.impl;

import dev.practice.shopapp.dto.*;
import dev.practice.shopapp.enums.OrderStatus;
import dev.practice.shopapp.mappers.OrderMapper;
import dev.practice.shopapp.models.*;
import dev.practice.shopapp.repositories.*;
import dev.practice.shopapp.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JpaOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    // 🚀 INJECTED BUSINESS RULES
    @Value("${app.finance.tax-rate}")
    private BigDecimal taxRate;

    @Value("${app.finance.shipping-flat-rate}")
    private BigDecimal shippingFlatRate;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        User currentUser = resolveUserWithAddresses();
        log.info("Processing order creation for user: {}", currentUser.getEmail());
        Address deliveryAddress = resolveAddressFromUser(currentUser, request.getAddressId());

        OrderEntity order = orderMapper.toOrderEntity(request);
        order.setUser(currentUser);
        order.setDeliveryAddress(deliveryAddress);
        order.setStatus(OrderStatus.PENDING);
        order.setReferenceCode("VEL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        BigDecimal runningTotal = BigDecimal.ZERO;
        String detectedCurrency = null; // 🚀 TO TRACK DYNAMIC CURRENCY

        for (OrderItemDto itemDto : request.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("PRODUCT NOT FOUND: " + itemDto.getProductId()));

            // 🚀 1. DYNAMIC CURRENCY ASSIGNMENT
            // We set the order's currency based on the products being purchased.
            if (detectedCurrency == null) {
                detectedCurrency = product.getCurrencyCode();
                order.setCurrencyCode(detectedCurrency);
            } else if (!detectedCurrency.equals(product.getCurrencyCode())) {
                // 🛡️ SECURITY CHECK: Prevent mixing USD and EUR in one basket
                throw new RuntimeException("MULTI-CURRENCY BASKETS ARE NOT SUPPORTED");
            }

            if (product.getQuantityInStock() < itemDto.getQuantity()) {
                throw new RuntimeException("INSUFFICIENT STOCK FOR: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setQuantity(itemDto.getQuantity());

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            orderItem.setTotalPrice(itemTotal);

            runningTotal = runningTotal.add(itemTotal);
            order.addOrderItem(orderItem);

            product.setQuantityInStock(product.getQuantityInStock() - itemDto.getQuantity());
        }

        order.setTotalPrice(runningTotal);
        order.setTaxAmount(runningTotal.multiply(taxRate));
        order.setShippingCost(shippingFlatRate);

        OrderEntity savedOrder = orderRepository.save(order);
        log.info("Order successfully created. Reference: {} | Total: {} {}",
                savedOrder.getReferenceCode(),
                savedOrder.getTotalPrice(),
                savedOrder.getCurrencyCode());

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersForCurrentUser() {
        User user = resolveUserWithAddresses();
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with given ID: " + orderId));

        log.info("Updating order {} status from {} to {}",
                order.getReferenceCode(), order.getStatus(), request.getStatus());

        order.setStatus(request.getStatus());
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    // --- REVISED PRIVATE RESOLVERS ---

    private User resolveUserWithAddresses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Using your custom query to get user + addresses in one hit
        return userRepository.findByEmail(email)
                .flatMap(u -> userRepository.findByIdWithAddresses(u.getId()))
                .orElseThrow(() -> new RuntimeException("USER IDENTITY NOT FOUND"));
    }

    private Address resolveAddressFromUser(User user, Long addressId) {
        // Search the User's address list for the provided ID
        return user.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ADDRESS ID " + addressId + " NOT LINKED TO CURRENT USER"));
    }
}