package dev.practice.shopapp.models;

import dev.practice.shopapp.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String referenceCode;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private String currencyCode;
    private BigDecimal taxAmount;
    private BigDecimal shippingCost;
    private Long userId;
    private List<OrderItem> items = new ArrayList<>();
    private Address deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
