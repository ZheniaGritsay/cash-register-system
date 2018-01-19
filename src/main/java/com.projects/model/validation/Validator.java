package com.projects.model.validation;

import com.projects.model.validation.exception.ValidationException;

import java.util.Set;

public interface Validator {
    Set<Violation> validate(Object object) throws ValidationException;
}
