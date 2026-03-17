package dev.practice.shopapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String referenceCode;
    private String status;
    private BigDecimal totalPrice;
    private String currencyCode;
    private BigDecimal taxAmount;
    private BigDecimal shippingCost;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
