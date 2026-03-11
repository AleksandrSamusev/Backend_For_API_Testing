package dev.practice.shopapp.dto;

import dev.practice.shopapp.enums.AddressType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    @NotBlank(message = "{address.streetAddress.required}")
    @Size(min = 5, max = 100, message = "{address.streetAddress.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "{address.streetAddress.invalid}")
    private String streetAddress;

    @Size(max = 50, message = "{address.apartment.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]*$", message = "{address.apartment.invalid}")
    private String apartment;

    @NotBlank(message = "{address.city.required}")
    @Size(min = 2, max = 50, message = "{address.city.size}")
    @Pattern(regexp = "^[a-zA-Z\\s.-]+$", message = "{address.city.invalid}")
    private String city;

    @Size(max = 50, message = "{address.state.size}")
    @Pattern(regexp = "^[a-zA-Z\\s.-]*$", message = "{address.state.invalid}")
    private String state;

    @NotBlank(message = "{address.postalCode.required}")
    @Size(min = 3, max = 10, message = "{address.postalCode.size}")
    @Pattern(regexp = "^[A-Z0-9\\s-]+$", message = "{address.postalCode.invalid}")
    private String postalCode;

    @NotBlank(message = "{address.countryCode.required}")
    @Pattern(regexp = "^[A-Z]{2}$", message = "{address.countryCode.sizeAndInvalid}")
    private String countryCode;

    @NotNull(message = "{address.type.required}")
    private AddressType addressType;
}
