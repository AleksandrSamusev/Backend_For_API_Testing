package dev.practice.shopapp.models;

import dev.practice.shopapp.validation.ValidEmail;
import dev.practice.shopapp.validation.ValidName;
import dev.practice.shopapp.validation.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
@Schema(description = "User entity representing a registered user in the system")
public class User {
    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @PositiveOrZero(message = "{user.id.invalid}")
    private Long id;
    @Schema(description = "User's legal first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidName(fieldName = "firstname")
    private String firstName;
    @Schema(description = "User's legal last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidName(fieldName = "lastname")
    private String lastName;
    @Schema(description = "Unique email address for account login", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidEmail
    private String email;
    @Schema(description = "Contact phone number in international format", example = "+1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidPhoneNumber
    private String phoneNumber;
    @Schema(description = "User's primary residential address")
    @Valid
    private Address address = new Address();

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String phoneNumber, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = (address != null) ? address: new Address();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        if (email != null) {
            // Sanitize value
            this.email = email.trim().toLowerCase();
        }
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
