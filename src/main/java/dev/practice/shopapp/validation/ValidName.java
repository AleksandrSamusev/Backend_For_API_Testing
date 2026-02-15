package dev.practice.shopapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {
    // New attribute to distinguish between first and last name
    String fieldName() default "name";

    String message() default "{user.name.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
