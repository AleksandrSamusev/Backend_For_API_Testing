package dev.practice.shopapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    public AddressDto() {
    }

    public AddressDto(String streetAddress, String apartment, String city, String state,
                      String postalCode, String countryCode) {
        this.streetAddress = streetAddress;
        this.apartment = apartment;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
