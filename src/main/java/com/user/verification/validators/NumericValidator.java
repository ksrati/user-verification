package com.user.verification.validators;

import org.springframework.stereotype.Component;

@Component
public class NumericValidator implements Validator {
    @Override
    public boolean validate(String input) {
        try {
            Integer.parseInt(input);
            return true; // If input can be parsed as an integer, it's numeric
        } catch (NumberFormatException e) {
            return false;
        }
    }
}