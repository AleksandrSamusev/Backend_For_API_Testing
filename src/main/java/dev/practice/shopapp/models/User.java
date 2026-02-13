package dev.practice.shopapp.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public class User {
    @Nullable
    @PositiveOrZero(message = "ID should be positive or zero")
    private Long id;
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "First name must only contain letters, hyphens, and spaces")
    private String firstName;
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "Last name must only contain letters, hyphens, and spaces")
    private String lastName;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 12, max = 16, message = "Phone number must have between 12 and 15 characters")
    @Pattern(regexp = "^\\+\\d{1,15}$", message = "Invalid phone number format. Use + and numbers (e.g., +1234567890)")
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
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
