package dev.practice.shopapp.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    @NotBlank @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8)
    private String password;

    @NotBlank
    private String phoneNumber;
}
