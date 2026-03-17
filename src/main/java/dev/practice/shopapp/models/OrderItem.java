package dev.practice.shopapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "{order.user.required}")
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "{orderItem.product.required}")
    private Product product;

    @NotBlank(message = "{product.name.required}")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull(message = "{orderItem.price.required}")
    @DecimalMin(value = "0.01", message = "{orderItem.price.min}")
    @Column(name = "unit_price", precision = 8, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull(message = "{orderItem.quantity.required}")
    @Min(value = 1, message = "{orderItem.quantity.min}")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "{order.totalPrice.required}")
    @Column(name = "total_price", precision = 8, scale = 2, nullable = false)
    private BigDecimal totalPrice;
}
