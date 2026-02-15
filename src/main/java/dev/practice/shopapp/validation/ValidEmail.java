package dev.practice.shopapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "{user.email.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
