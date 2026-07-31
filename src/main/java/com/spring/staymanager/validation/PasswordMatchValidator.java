package com.spring.staymanager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String passwordFieldName;
    private String confirmPasswordFieldName;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.passwordFieldName = "password";
        this.confirmPasswordFieldName = "confirmPassword";
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Object password = beanWrapper.getPropertyValue(passwordFieldName);
        Object confirmPassword = beanWrapper.getPropertyValue(confirmPasswordFieldName);

        if (password == null || confirmPassword == null) {
            return true;
        }

        boolean isValid = password.equals(confirmPassword);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(confirmPasswordFieldName)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
