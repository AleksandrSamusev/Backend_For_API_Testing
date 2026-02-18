package dev.practice.shopapp.dto;

import dev.practice.shopapp.validation.ValidEmail;
import dev.practice.shopapp.validation.ValidName;
import dev.practice.shopapp.validation.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

public class UserUpdateDto {
    @Schema(description = "User's legal first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidName(fieldName = "firstname")
    private String firstName;
    @Schema(description = "User's legal last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidName(fieldName = "lastname")
    private String lastName;
    @Schema(description = "Unique email address for account login",
            example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidEmail
    private String email;
    @Schema(description = "Contact phone number in international format",
            example = "+1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidPhoneNumber
    private String phoneNumber;
    @Valid
    @Schema(description = "Primary user's residential address")
    private AddressDto address = new AddressDto();

    public UserUpdateDto() {
    }

    public UserUpdateDto(String firstName, String lastName, String email, String phoneNumber, AddressDto address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = (address != null) ? address : new AddressDto();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null) {
            // Sanitize value
            this.email = email.trim().toLowerCase();
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto addressDto) {
        this.address = addressDto;
    }
}
