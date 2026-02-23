package dev.practice.shopapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.practice.shopapp.enums.AvailabilityStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@AllArgsConstructor // Required for Builder
@Builder // The "Pro" way to instantiate
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Only compare ID/SKU
public class Product {
    @EqualsAndHashCode.Include
    private Long id;

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
    @Setter(AccessLevel.PROTECTED)
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
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.PROTECTED)
    private AvailabilityStatus status;

    @NotNull(message = "{product.createdAt.required}")
    private LocalDateTime createdAt;

    @NotBlank(message = "{product.createdBy.required}")
    @Size(min = 3, max = 50, message = "{product.createdBy.size}")
    private String createdBy;

    private LocalDateTime updatedAt;

    @Size(min = 3, max = 50, message = "{product.updatedBy.size}")
    private String updatedBy;

    @PositiveOrZero(message = "{product.version.format}")
    @Builder.Default
    private Long version = 0L;

    /**
     * Resetting to In-Stock usually clears the expected date.
     */
    public void updateStock(int newQuantity) {
        this.quantityInStock = newQuantity;

        if (newQuantity > 0) {
            // If it was BACKORDER or OUT_OF_STOCK, it's now IN_STOCK
            this.status = AvailabilityStatus.IN_STOCK;
            this.expectedAvailabilityDate = null; // Clear the date, it's here now!
        } else {
            // Only move to OUT_OF_STOCK if it wasn't already a deliberate BACKORDER
            if (this.status != AvailabilityStatus.BACKORDER) {
                this.status = AvailabilityStatus.OUT_OF_STOCK;
            }
        }
    }

    /**
     * Triggers Preorder state.
     *
     * @param releaseDate The date the product will officially ship.
     */
    public void startPreorder(LocalDateTime releaseDate) {
        if (releaseDate == null || releaseDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Release date must be in the future.");
        }
        this.status = AvailabilityStatus.PREORDER;
        this.expectedAvailabilityDate = releaseDate;
    }

    /**
     * Switches the product to Backorder mode.
     * @param restockDate When we expect the new shipment to arrive.
     */
    public void startBackorder(LocalDateTime restockDate) {
        // Business Rule: You can't backorder if you still have items in stock!
        if (this.quantityInStock > 0) {
            throw new IllegalStateException("Cannot backorder while items are still in stock.");
        }

        // Business Rule: You must tell the customer when it's coming
        if (restockDate == null || restockDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Restock date must be in the future.");
        }

        this.status = AvailabilityStatus.BACKORDER;
        this.expectedAvailabilityDate = restockDate;
    }

    public Product deleteProduct(String performingUser) {
        this.setStatus(AvailabilityStatus.ARCHIVED);
        this.setUpdatedAt(LocalDateTime.now());
        this.setUpdatedBy(performingUser);
        return this;
    }
}


