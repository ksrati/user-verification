package com.user.verification.validators;

import org.springframework.stereotype.Component;

@Component
public class EnglishAlphabetsValidator implements Validator {
    @Override
    public boolean validate(String input) {
        return input.matches("[a-zA-Z]+"); // Checks if the input contains only alphabets
    }
}