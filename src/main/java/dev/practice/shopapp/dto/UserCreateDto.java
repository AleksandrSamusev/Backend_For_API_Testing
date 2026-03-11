package dev.practice.shopapp.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Data Transfer Object for creating a new user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @Schema(description = "User's legal first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.firstname.required}")
    @Size(min = 2, max = 50, message = "{user.firstname.size}")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "{user.firstname.pattern}")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Schema(description = "User's legal last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.lastname.required}")
    @Size(min = 2, max = 50, message = "{user.lastname.size}")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "{user.lastname.pattern}")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Schema(description = "Unique email address for account login", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.email.required}")
    @Email(message = "{user.email.invalid}")
    @Column(nullable = false, unique = true)
    private String email;

    @Schema(description = "Contact phone number in international format", example = "+1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{user.phoneNumber.required}")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "{user.phoneNumber.invalid}")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Schema(description = "User's primary residential address")
    @Valid
    @NotNull(message = "Address details are required")
    private List<AddressDto> addresses = new ArrayList<>();
}
