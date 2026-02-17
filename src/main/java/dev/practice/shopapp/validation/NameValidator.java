package dev.practice.shopapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.util.HtmlUtils;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    private String fieldName;

    @Override
    public void initialize(ValidName constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation(); 

        // --- 1. SANITIZATION ---
        if (value != null) {
            // Trim whitespace and Escape HTML characters (XSS Protection)
            value = HtmlUtils.htmlEscape(value.trim());
        }

        // --- 2. VALIDATION ---
        // Check if Blank (after trim)
        if (value == null || value.isBlank()) {
            context.buildConstraintViolationWithTemplate("{user." + fieldName + ".required}")
                    .addConstraintViolation();
            return false;
        }

        boolean isValid = true;

        // Check Size
        if (value.length() < 2 || value.length() > 50) {
            addViolation(context, "user." + fieldName + ".size");
            isValid = false;
        }

        // Check Pattern
        // Prev - ^[a-zA-Z -]+$
        //For internationalization - ^[a-zA-Z\u00C0-\u017F -]+$
        if (!value.matches("^[a-zA-Z -]+$")) {
            addViolation(context, "user." + fieldName + ".pattern");
            isValid = false;
        }

        return isValid;
    }

    private void addViolation(ConstraintValidatorContext context, String messageKey) {
        context.buildConstraintViolationWithTemplate("{" + messageKey + "}")
                .addConstraintViolation();
    }
}
