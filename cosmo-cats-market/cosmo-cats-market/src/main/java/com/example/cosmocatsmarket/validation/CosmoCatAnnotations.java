package com.example.cosmocatsmarket.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
// Where we can apply anotation
@Target({ ElementType.FIELD, ElementType.PARAMETER })
// How long anotation is kept
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CosmoCatValidator.class)
public @interface CosmoCatAnotations {
    String message() default "Products name must include a cosmic terms like 'star', 'galaxy', 'comet', and etc.";
}
