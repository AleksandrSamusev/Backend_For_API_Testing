package dev.practice.shopapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    @NotBlank(message = "Identity (Email) is required")
    @Email(message = "Invalid Comm-Link format")
    private String email;

    @NotBlank(message = "Access Cipher (Password) is required")
    private String password;
}
