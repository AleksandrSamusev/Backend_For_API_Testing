package dev.practice.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.practice.shopapp.enums.AvailabilityStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {
    @NotBlank(message = "{product.name.required}")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\-_/\\\\.()&'!]+$", message = "{product.name.invalid}")
    @Size(min = 2, max = 150, message = "{product.name.size}")
    private String name;

    @NotBlank(message = "{product.category.required}")
    @Size(min = 2, max = 50, message = "{product.category.size}")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\-_/&'.()]+$", message = "{product.category.invalid}")
    private String category;

    @NotBlank(message = "{product.manufacturer.required}")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\-_/\\\\.()&'!]+$", message = "{product.manufacturer.invalid}")
    @Size(min = 2, max = 150, message = "{product.manufacturer.size}")
    private String manufacturer;

    @NotNull(message = "{product.price.required}")
    @DecimalMin(value = "0.01", message = "{product.price.min}")
    @DecimalMax(value = "999999.99", message = "{product.price.max}")
    @Digits(integer = 6, fraction = 2, message = "{product.price.format}")
    private BigDecimal price;

    @NotNull(message = "{product.cost.required}")
    @DecimalMin(value = "0.01", message = "{product.cost.min}")
    @DecimalMax(value = "999999.99", message = "{product.cost.max}")
    @Digits(integer = 6, fraction = 2, message = "{product.cost.format}")
    private BigDecimal costPrice;

    @DecimalMin(value = "0.01", message = "{product.sale.min}")
    @DecimalMax(value = "999999.99", message = "{product.sale.max}")
    @Digits(integer = 6, fraction = 2, message = "{product.sale.format}")
    private BigDecimal salePrice;

    @NotBlank(message = "{product.currency.required}")
    @Pattern(regexp = "^[A-Z]{3}$", message = "{product.currency.invalid}")     // ISO 4217
    private String currencyCode;

    @NotNull(message = "{product.quantity.required}")
    @Min(value = 0, message = "{product.quantity.min}")
    @Max(value = 999999, message = "{product.quantity.max}")
    private Integer quantityInStock;

    @NotNull(message = "{product.threshold.require}")
    @Min(value = 1, message = "{product.threshold.min}")
    private Integer lowStockThreshold;

    @Future(message = "{product.availabilityDate.future}")
    private LocalDateTime expectedAvailabilityDate;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Builder.Default // Prevents builder from nullifying your map
    private Map<String, Object> attributes = new HashMap<>();

    @URL(message = "{product.imageUrl.invalid}")
    private String imageUrl;

    @NotNull(message = "{product.status.required}")
    private AvailabilityStatus status;

    @NotNull(message = "{product.version.required}")
    @PositiveOrZero(message = "{product.version.invalid}")
    private Long version;
}
