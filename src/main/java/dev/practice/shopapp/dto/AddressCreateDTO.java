package dev.practice.shopapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AddressCreateDTO {
    @NotBlank
    @Size(min = 5, max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Street address contains invalid characters")
    private String streetAddress;
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]*$", message = "Apartment contains invalid characters")
    private String apartment;
    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s.-]+$", message = "City contains invalid characters")
    private String city;
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s.-]*$", message = "State contains invalid characters")
    private String state;
    @NotBlank
    @Size(min = 3, max = 10)
    @Pattern(regexp = "^[A-Z0-9\\s-]+$", message = "Postal code must be uppercase letters, numbers, spaces or hyphens")
    private String postalCode;
    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be exactly 2 uppercase letters")
    private String countryCode;

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
