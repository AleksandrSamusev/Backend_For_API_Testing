package dev.practice.shopapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    // PRO Regex:
    // 1. Allows standard characters
    // 2. Requires @
    // 3. Requires a dot in the domain (prevents @localhost)
    // 4. Requires a TLD of 2-6 characters (e.g., .com, .museum)
    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        // 1. Basic Null/Blank check
        if (value == null || value.isBlank()) {
            addViolation(context, "{user.email.required}");
            return false;
        }

        // 2. SANITIZATION
        // Trim spaces and normalize case (Email parts: LOCAL@DOMAIN)
        String sanitizedEmail = value.trim();

        // Split to normalize ONLY the domain part (Local parts CAN be case sensitive, though rare)
        String[] parts = sanitizedEmail.split("@");
        if (parts.length == 2) {
            sanitizedEmail = parts[0] + "@" + parts[1].toLowerCase();
        }

        // 3. LENGTH VALIDATION (OWASP Rule #1)
        if (sanitizedEmail.length() > 254) {
            addViolation(context, "{user.email.tooLong}");
            return false;
        }

        // 4. REGEX VALIDATION (OWASP Rule #2)
        if (!sanitizedEmail.matches(EMAIL_REGEX)) {
            addViolation(context, "{user.email.invalid}");
            return false;
        }

        return true;
    }

    private void addViolation(ConstraintValidatorContext context, String template) {
        context.buildConstraintViolationWithTemplate(template)
                .addConstraintViolation();
    }
}
