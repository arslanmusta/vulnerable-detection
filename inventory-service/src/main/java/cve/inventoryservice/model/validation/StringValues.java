package cve.inventoryservice.model.validation;

import cve.inventoryservice.model.validation.implementation.StringValuesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StringValuesValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StringValues {
    String[] allowedValues();
    String message() default "Invalid string value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
