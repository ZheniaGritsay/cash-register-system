package com.projects.model.validation.impl;

import com.projects.model.validation.Validator;
import com.projects.model.validation.ValidatorFactory;

public class ValidatorFactoryImpl implements ValidatorFactory {
    private static final ValidatorFactoryImpl instance = new ValidatorFactoryImpl();
    private final ValidatorImpl validator = new ValidatorImpl();

    private ValidatorFactoryImpl() {}

    @Override
    public Validator getValidator() {
        return validator;
    }

    public static ValidatorFactoryImpl getInstance() {
        return instance;
    }
}
