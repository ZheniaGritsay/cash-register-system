package com.projects.model.validation.impl;

import com.projects.model.validation.Validator;
import com.projects.model.validation.ValidatorFactory;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class ValidatorFactoryTest {
    private ValidatorFactory validatorFactory = ValidatorFactoryImpl.getInstance();

    @Test
    public void getValidator() {
        Validator validator = validatorFactory.getValidator();
        Validator anotherValidator = validatorFactory.getValidator();

        assertSame(validator, anotherValidator);
    }
}
