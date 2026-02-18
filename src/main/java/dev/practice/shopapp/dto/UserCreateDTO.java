package dev.practice.shopapp.dto;

import dev.practice.shopapp.validation.ValidEmail;
import dev.practice.shopapp.validation.ValidName;
import dev.practice.shopapp.validation.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data Transfer Object for creating a new user")
public class UserCreateDTO {
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
    @NotNull(message = "Address details are required")
    private AddressCreateDTO address = new AddressCreateDTO();

    public UserCreateDTO() {
    }

    public UserCreateDTO(String firstName, String lastName, String email, String phoneNumber,
                         AddressCreateDTO address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = (address != null) ? address : new AddressCreateDTO();
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

    public AddressCreateDTO getAddressCreateDTO() {
        return address;
    }

    public void setAddressCreateDTO(AddressCreateDTO addressCreateDTO) {
        this.address = addressCreateDTO;
    }
}
