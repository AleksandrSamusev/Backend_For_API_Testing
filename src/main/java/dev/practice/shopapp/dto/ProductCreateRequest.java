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
public class ProductCreateRequest {
    @NotBlank(message = "{product.name.required}")
    @Pattern(regexp = "^[a-zA-Z .-]+$", message = "{product.name.invalid}")
    @Size(min = 2, max = 150, message = "{product.name.size}")
    private String name;

    @NotBlank(message = "{product.category.required}")
    @Size(min = 2, max = 50, message = "{product.category.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_/&'.()]+$", message = "{product.category.invalid}")
    private String category;

    @NotBlank(message = "{product.manufacturer.required}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_/&'.()]+$", message = "{product.manufacturer.invalid}")
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

    @NotBlank(message = "{product.sku.required}")
    @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "{product.sku.invalid}")
    @Size(min = 6, max = 30, message = "{product.sku.size}")
    private String sku;

    @NotNull(message = "{product.quantity.required}")
    @Min(value = 0, message = "{product.quantity.min}")
    @Max(value = 999999, message = "{product.quantity.max}")
    private Integer quantityInStock;

    @NotNull(message = "{product.threshold.require}")
    @Min(value = 1, message = "{product.threshold.min}")
    @Max(value = 999999, message = "{product.threshold.max}")
    private Integer lowStockThreshold;

    @Future(message = "{product.availabilityDate.future}")
    private LocalDateTime expectedAvailabilityDate;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Builder.Default // Prevents builder from nullifying your map
    private Map<String, Object> attributes = new HashMap<>();

    @URL(regexp = "^(https?|ftp|file):\\/\\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)?((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])(\\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}))(:[0-9]+)?(\\/.*)?$",
            message = "{product.imageUrl.invalid}")
    private String imageUrl;

    @NotNull(message = "{product.status.required}")
    private AvailabilityStatus status;

    @NotBlank(message = "{product.createdBy.required}")
    @Size(min = 3, max = 50, message = "{product.createdBy.size}")
    private String createdBy;
}
