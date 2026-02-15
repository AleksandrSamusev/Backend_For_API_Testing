package dev.practice.shopapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final String REGEX = "^\\+\\d{11,15}$";
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        // 1. Disable the default @ValidPhoneNumber message
        context.disableDefaultConstraintViolation();

        // 2. Handle Null/Blank (Replaces @NotBlank)
        if (phoneNumber == null || phoneNumber.isBlank()) {
            context.buildConstraintViolationWithTemplate("{user.phone.required}")
                    .addConstraintViolation();
            return false;
        }

        // 3. SANITIZATION: Trim any accidental spaces
        String sanitizedPhone = phoneNumber.trim();

        // 4. VALIDATION: Check the Regex
        if (!sanitizedPhone.matches(REGEX)) {
            context.buildConstraintViolationWithTemplate("{user.phone.invalid}")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
