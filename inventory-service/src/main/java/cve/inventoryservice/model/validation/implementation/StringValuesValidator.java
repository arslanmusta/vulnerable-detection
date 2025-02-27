package cve.inventoryservice.model.validation.implementation;

import cve.inventoryservice.model.validation.StringValues;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class StringValuesValidator implements ConstraintValidator<StringValues, String> {
    private List<String> allowedValues;

    @Override
    public void initialize(StringValues constraintAnnotation) {
        allowedValues = Arrays.stream(constraintAnnotation.allowedValues()).map(String::toUpperCase).toList();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || allowedValues.contains(value.toUpperCase());
    }
}
