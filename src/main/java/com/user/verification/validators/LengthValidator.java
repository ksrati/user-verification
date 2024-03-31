package com.user.verification.validators;

import org.springframework.stereotype.Component;

@Component

public class LengthValidator implements Validator 
{
	@Override
    public boolean validate(String input) {
        try {
            int value = Integer.parseInt(input);
            return value >= 1 && value <= 5; // Check if the value is within the range of 1-5
        } catch (NumberFormatException e) {
            return false; // If parsing fails, it's not a valid numeric input
        }
    }
}
