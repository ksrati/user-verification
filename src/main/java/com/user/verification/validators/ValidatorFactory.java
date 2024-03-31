package com.user.verification.validators;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

@Component
public class ValidatorFactory {
	 private final LengthValidator lengthValidator;
    private final NumericValidator numericValidator;
    private final EnglishAlphabetsValidator englishAlphabetsValidator;

    @Autowired
    public ValidatorFactory(LengthValidator lengthValidator ,NumericValidator numericValidator, EnglishAlphabetsValidator englishAlphabetsValidator) {
        this.numericValidator = numericValidator;
        this.englishAlphabetsValidator = englishAlphabetsValidator;
        this.lengthValidator=lengthValidator;
    }

    public Validator getValidator(String paramType) {
        if (paramType.equals("offset")) {
            return numericValidator;
        }else if(paramType.equals("limit")||paramType.equals("size") ){
        	return lengthValidator;        	
        }
        else if (paramType.equals("sortType") || paramType.equals("sortOrder")) {
            return englishAlphabetsValidator;
        }
        // Handle other cases or invalid types
        return null;
    }
}

