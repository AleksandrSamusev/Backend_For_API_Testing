package dev.practice.shopapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.practice.shopapp.enums.AddressType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
// 🚀 THE FIX: Standardize on 'Explicitly Included' and remove the old 'exclude' parameters
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // ✅ Included: Safe for Identity
    @ToString.Include          // ✅ Included: Safe for Logs
    private Long id;

    @NotBlank(message = "{address.streetAddress.required}")
    @Size(min = 5, max = 100, message = "{address.streetAddress.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "{address.streetAddress.invalid}")
    @Column(name = "street_address", nullable = false, length = 100)
    @ToString.Include // Included to see the street in logs
    private String streetAddress;

    @Size(max = 50, message = "{address.apartment.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]*$", message = "{address.apartment.invalid}")
    @Column(name = "apartment", length = 50)
    @ToString.Include
    private String apartment;

    @NotBlank(message = "{address.city.required}")
    @Size(min = 2, max = 50, message = "{address.city.size}")
    @Pattern(regexp = "^[a-zA-Z\\s.-]+$", message = "{address.city.invalid}")
    @Column(name = "city", nullable = false, length = 50)
    @ToString.Include
    private String city;

    @Size(max = 50, message = "{address.state.size}")
    @Pattern(regexp = "^[a-zA-Z\\s.-]*$", message = "{address.state.invalid}")
    @Column(name = "state", length = 50)
    @ToString.Include
    private String state;

    @NotBlank(message = "{address.postalCode.required}")
    @Size(min = 3, max = 10, message = "{address.postalCode.size}")
    @Pattern(regexp = "^[A-Z0-9\\s-]+$", message = "{address.postalCode.invalid}")
    @Column(name = "postal_code", nullable = false, length = 10)
    @ToString.Include
    private String postalCode;

    @NotBlank(message = "{address.countryCode.required}")
    @Pattern(regexp = "^[A-Z]{2}$", message = "{address.countryCode.sizeAndInvalid}")
    @Column(name = "country_code", nullable = false, length = 2)
    @ToString.Include
    private String countryCode;

    @NotNull(message = "{address.type.required}")
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false, length = 20)
    @ToString.Include
    private AddressType addressType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Address must belong to a user")
    @JsonBackReference // 🛡️ JSON Symmetry: Stops Jackson recursion
    // 🛡️ LOMBOK SYMMETRY: By NOT adding @Include here, we successfully exclude it
    private User user;
}