package dev.practice.shopapp.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.practice.shopapp.enums.AddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User entity representing a registered user in the system")
@Table(name = "users")
@Entity
public class User {
    @Id
    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "User's legal first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.firstname.required}")
    @Size(min = 2, max = 50, message = "{user.firstname.size}")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "{user.firstname.pattern}")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Schema(description = "User's legal last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.lastname.required}")
    @Size(min = 2, max = 50, message = "{user.lastname.size}")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "{user.lastname.pattern}")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Schema(description = "Unique email address for account login", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.email.required}")
    @Email(message = "{user.email.invalid}")
    @Column(nullable = false, unique = true, columnDefinition = "text")
    private String email;

    @Schema(description = "Contact phone number in international format", example = "+1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.phoneNumber.required}")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "{user.phoneNumber.invalid}")
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Schema(description = "A list of user's registered addresses")
    @Valid
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // The "Forward" part of the relationship
    private List<Address> addresses = new ArrayList<>();

    public void addAddress(Address address) {
        // 1. If the new address is PRIMARY or BILLING,
        // we make sure no other address in the list has that type.
        if(address.getAddressType() == AddressType.PRIMARY || address.getAddressType() == AddressType.BILLING) {
            addresses.stream()
                    .filter(a -> a.getAddressType() == address.getAddressType())
                    .forEach(a -> a.setAddressType(AddressType.SHIPPING));
        }
        addresses.add(address);
        address.setUser(this); // Updates the "Many" side (Foreign Key)
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null); // Breaks the link for orphanRemoval to work
    }
}
