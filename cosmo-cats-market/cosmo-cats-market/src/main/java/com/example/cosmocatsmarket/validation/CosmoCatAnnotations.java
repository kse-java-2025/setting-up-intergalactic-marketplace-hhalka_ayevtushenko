package com.example.cosmocatsmarket.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CosmoCatValidator.class)
public @interface CosmoCatAnnotations {
    String message() default "Products name must include a cosmic terms like 'star', 'galaxy', 'comet', and etc.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
