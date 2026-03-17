package dev.practice.shopapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemDto {
    @NotNull(message = "{orderItem.product.required}")
    private Long productId;

    @Min(value = 1, message = "{orderItem.quantity.min}")
    @NotNull(message = "{orderItem.quantity.required}")
    private Integer quantity;
}
