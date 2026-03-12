package dev.practice.shopapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.practice.shopapp.enums.AvailabilityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
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
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{product.name.required}")
    @Pattern(regexp = "^[a-zA-Z0-9 .-]+$", message = "{product.name.invalid}")
    @Size(min = 2, max = 150, message = "{product.name.size}")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotBlank(message = "{product.category.required}")
    @Size(min = 2, max = 50, message = "{product.category.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_/&'.()]+$", message = "{product.category.invalid}")
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @NotBlank(message = "{product.manufacturer.required}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_/&'.()]+$", message = "{product.manufacturer.invalid}")
    @Size(min = 2, max = 150, message = "{product.manufacturer.size}")
    @Column(name = "manufacturer", nullable = false, length = 150)
    private String manufacturer;

    @NotNull(message = "{product.price.required}")
    @DecimalMin(value = "0.01", message = "{product.price.min}")
    @DecimalMax(value = "999999.99", message = "{product.price.max}")
    @Digits(integer = 6, fraction = 2, message = "{product.price.format}")
    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @NotNull(message = "{product.cost.required}")
    @DecimalMin(value = "0.01", message = "{product.cost.min}")
    @DecimalMax(value = "999999.99", message = "{product.cost.max}")
    @Digits(integer = 6, fraction = 2, message = "{product.cost.format}")
    @Column(name = "cost_price", nullable = false, precision = 8, scale = 2)
    private BigDecimal costPrice;

    @DecimalMin(value = "0.01", message = "{product.sale.min}")
    @DecimalMax(value = "999999.99", message = "{product.sale.max}")
    @Digits(integer = 6, fraction = 2, message = "{product.sale.format}")
    @Column(name = "sale_price", precision = 8, scale = 2)
    private BigDecimal salePrice;

    @NotBlank(message = "{product.currency.required}")
    @Pattern(regexp = "^[A-Z]{3}$", message = "{product.currency.invalid}")
    @JdbcTypeCode(SqlTypes.CHAR) // FIX: Explicitly tell Hibernate this is a CHAR type
    @Column(name = "currency_code", nullable = false, columnDefinition = "char(3)")
    private String currencyCode;

    @NotBlank(message = "{product.sku.required}")
    @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "{product.sku.invalid}")
    @Size(min = 6, max = 30, message = "{product.sku.size}")
    @Column(name = "sku", nullable = false, unique = true, length = 30)
    private String sku;

    @NotNull(message = "{product.quantity.required}")
    @Min(value = 0, message = "{product.quantity.min}")
    @Max(value = 999999, message = "{product.quantity.max}")
    @Setter(AccessLevel.NONE)
    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @NotNull(message = "{product.threshold.require}")
    @Min(value = 1, message = "{product.threshold.min}")
    @Max(value = 999999, message = "{product.threshold.max}")
    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold;

    @Future(message = "{product.availabilityDate.future}")
    @Column(name = "expected_availability_date")
    private LocalDateTime expectedAvailabilityDate;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private Map<String, Object> attributes = new HashMap<>();


    @Pattern(
            // Allows standard http/https URLs OR Base64 data URIs
            regexp = "^(https?|ftp|file):\\/\\/.*|^data:image\\/(png|jpeg|jpg|webp);base64,.*",
            message = "{product.imageUrl.invalid}"
    )
    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @NotNull(message = "{product.status.required}")
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    @Column(name = "status", nullable = false, length = 20)
    private AvailabilityStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotBlank(message = "{product.createdBy.required}")
    @Size(min = 3, max = 50, message = "{product.createdBy.size}")
    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Size(min = 3, max = 50, message = "{product.updatedBy.size}")
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Version
    @Column(name = "version", nullable = false) // FIX: Matches SQL 'NOT NULL'
    @Builder.Default
    private Long version = 0L;

    public void setQuantityInStock(Integer newQuantity) {
        this.quantityInStock = newQuantity;
        // Your "Human Factor Insurance" logic here:
        if (newQuantity != null && newQuantity > 0) {
            this.status = AvailabilityStatus.IN_STOCK;
            this.expectedAvailabilityDate = null;
        } else if (newQuantity != null && this.status != AvailabilityStatus.BACKORDER) {
            this.status = AvailabilityStatus.OUT_OF_STOCK;
        }
    }

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
}


