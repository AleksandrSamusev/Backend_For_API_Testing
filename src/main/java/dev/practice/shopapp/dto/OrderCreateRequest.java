package dev.practice.shopapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {
    @NotNull(message = "{order.address.required}")
    private Long addressId;

    @NotEmpty(message = "{order.items.empty}")
    @Valid
    private List<OrderItemDto> items;
}

