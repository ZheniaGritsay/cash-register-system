package com.projects.model.validation.impl;

import com.projects.model.domain.constant.QuantityType;
import com.projects.model.domain.dto.Product;
import com.projects.model.validation.Validator;
import com.projects.model.validation.Violation;
import com.projects.model.validation.exception.ValidationException;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Set;

public class ValidatorTest {
    private Validator validator = new ValidatorImpl();

    @Test
    public void validateNoViolations() throws ValidationException {
        Product product = new Product.Builder()
                .id(1L)
                .title("Sugar")
                .code(3452L)
                .price(0.4)
                .boughtQuantity(200)
                .quantityOnStock(500)
                .quantityType(QuantityType.GRAM)
                .build();

        Set<Violation> violations = validator.validate(product);

        assertEquals(0, violations.size());
    }

    @Test
    public void validateViolationsPresent() throws ValidationException {
        Product product = new Product.Builder()
                .title("")
                .code(-1L)
                .price(0.00)
                .boughtQuantity(-1)
                .quantityOnStock(-1)
                .quantityType(null)
                .build();

        Set<Violation> violations = validator.validate(product);

        assertEquals(6, violations.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentException() throws ValidationException {
        validator.validate(null);
    }
}
