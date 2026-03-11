package dev.practice.shopapp.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @Schema(description = "User's legal first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 2, max = 50, message = "{user.firstname.size}")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "{user.firstname.pattern}")
    private String firstName;

    @Schema(description = "User's legal last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 2, max = 50, message = "{user.lastname.size}")
    @Pattern(regexp = "^[a-zA-Z -]+$", message = "{user.lastname.pattern}")
    private String lastName;

    @Schema(description = "Unique email address for account login",
            example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "{user.email.invalid}")
    private String email;

    @Schema(description = "Contact phone number in international format",
            example = "+1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "{user.phoneNumber.invalid}")
    private String phoneNumber;

    @Valid // Critical: Still validates AddressDto fields if a list is provided
    @Schema(description = "Primary user's residential address")
    private List<AddressDto> addresses;

}
