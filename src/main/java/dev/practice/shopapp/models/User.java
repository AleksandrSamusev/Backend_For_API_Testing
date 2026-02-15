package dev.practice.shopapp.models;

import dev.practice.shopapp.validation.ValidEmail;
import dev.practice.shopapp.validation.ValidName;
import dev.practice.shopapp.validation.ValidPhoneNumber;
import jakarta.validation.constraints.*;

public class User {
    @PositiveOrZero(message = "{user.id.invalid}")
    private Long id;
    @ValidName(fieldName = "firstname")
    private String firstName;
    @ValidName(fieldName = "lastname")
    private String lastName;
    @ValidEmail
    private String email;
    @ValidPhoneNumber
    private String phoneNumber;

    public User(Long id, String firstName, String lastName, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
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


}
