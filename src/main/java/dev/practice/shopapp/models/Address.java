package dev.practice.shopapp.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class Address {
    @NotBlank(message = "Street address must not be blank")
    @Size(min = 5, max = 100, message = "{address.streetAddress.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Street address contains invalid characters")
    private String streetAddress;
    @Size(max = 50, message = "{address.apartment.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]*$", message = "Apartment contains invalid characters")
    private String apartment;
    @NotBlank(message = "City must not be blank")
    @Size(min = 2, max = 50, message = "{address.city.size}")
    @Pattern(regexp = "^[a-zA-Z\\s.-]+$", message = "City contains invalid characters")
    private String city;
    @Size(max = 50, message = "{address.state.size}")
    @Pattern(regexp = "^[a-zA-Z\\s.-]*$", message = "State contains invalid characters")
    private String state;
    @NotBlank(message = "Postal Code must not be blank")
    @Size(min = 3, max = 10, message = "{address.postalCode.size}")
    @Pattern(regexp = "^[A-Z0-9\\s-]+$", message = "Postal code must be uppercase letters, numbers, spaces or hyphens")
    private String postalCode;
    @NotBlank(message = "Country code must not be blank")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be exactly 2 uppercase letters")
    private String countryCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Address() {
    }

    public Address(String streetAddress, String apartment, String city, String state,
                   String postalCode, String countryCode, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.streetAddress = streetAddress;
        this.apartment = apartment;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}