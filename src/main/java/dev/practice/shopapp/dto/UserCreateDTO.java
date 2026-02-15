package dev.practice.shopapp.dto;

import dev.practice.shopapp.validation.ValidEmail;
import dev.practice.shopapp.validation.ValidName;
import dev.practice.shopapp.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public class UserCreateDTO {
    @ValidName(fieldName = "firstname")
    private String firstName;
    @ValidName(fieldName = "lastname")
    private String lastName;
    @ValidEmail
    private String email;
    @ValidPhoneNumber
    private String phoneNumber;

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
}
