package dev.practice.shopapp.mappers;

import dev.practice.shopapp.dto.OrderCreateRequest;
import dev.practice.shopapp.dto.OrderItemResponse;
import dev.practice.shopapp.dto.OrderResponse;
import dev.practice.shopapp.models.OrderEntity;
import dev.practice.shopapp.models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderEntity toOrderEntity(OrderCreateRequest dto) {
        if (dto == null) return null;
        OrderEntity entity = new OrderEntity();
        entity.setOrderItems(new ArrayList<>());
        return entity;
    }

    public OrderResponse toOrderResponse(OrderEntity entity) {
        if (entity == null) return null;

        OrderResponse response = new OrderResponse();
        response.setId(entity.getId());
        response.setReferenceCode(entity.getReferenceCode());
        response.setStatus(entity.getStatus().name()); // Maps Enum to String
        response.setTotalPrice(entity.getTotalPrice());
        response.setCurrencyCode(entity.getCurrencyCode());
        response.setTaxAmount(entity.getTaxAmount());
        response.setShippingCost(entity.getShippingCost());
        response.setCreatedAt(entity.getCreatedAt());
        if (entity.getOrderItems() != null) {
            response.setItems(entity.getOrderItems().stream()
                    .map(this::toOrderItemResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private OrderItemResponse toOrderItemResponse(OrderItem item) {
        if (item == null) return null;

        OrderItemResponse response = new OrderItemResponse();

        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProductName());
        response.setUnitPrice(item.getUnitPrice());
        response.setQuantity(item.getQuantity());
        response.setTotalPrice(item.getTotalPrice());

        return response;
    }

    private String safeStrip(String value) {
        return value != null ? value.strip() : null;
    }
}
