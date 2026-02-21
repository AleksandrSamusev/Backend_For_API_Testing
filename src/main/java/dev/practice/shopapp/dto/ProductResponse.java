package dev.practice.shopapp.dto;

import dev.practice.shopapp.enums.AvailabilityStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String category;
    private String manufacturer;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String currencyCode;
    private String sku;
    private Integer quantityInStock;
    private LocalDateTime expectedAvailabilityDate;
    private Map<String, Object> attributes = new HashMap<>();
    private String imageUrl;
    private AvailabilityStatus status;
}
