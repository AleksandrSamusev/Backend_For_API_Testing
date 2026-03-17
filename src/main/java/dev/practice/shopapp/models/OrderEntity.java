package dev.practice.shopapp.models;

import dev.practice.shopapp.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{order.reference.required}")
    @Size(min = 5, max = 50, message = "{order.reference.size}")
    @Column(name = "reference_code", unique = true, nullable = false, length = 50)
    private String referenceCode;

    @NotNull(message = "{order.status.required}")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    @NotNull(message = "{order.totalPrice.required}")
    @DecimalMin(value = "0.01", message = "{order.totalPrice.min}")
    @Column(name = "total_price", precision = 8, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @NotBlank(message = "{order.currency.required}")
    @Pattern(regexp = "^[A-Z]{3}$", message = "{order.currency.invalid}")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "currency_code", nullable = false, columnDefinition = "char(3)")
    private String currencyCode;

    @DecimalMin(value = "0.00", message = "{order.tax.min}")
    @Column(name = "tax_amount", precision = 8, scale = 2)
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.00", message = "{order.shipping.min}")
    @Column(name = "shipping_cost", precision = 8, scale =2)
    private BigDecimal shippingCost;

    @NotNull(message = "{order.user.required}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotEmpty(message = "{order.items.empty}")
    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @NotNull(message = "{order.address.required}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address deliveryAddress;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Helper method for the "Handshake"
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrderEntity(this);
    }
}
