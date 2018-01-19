package com.projects.model.validation.impl;

import com.projects.model.validation.Violation;

public class ViolationImpl implements Violation {
    private String field;
    private String message;

    public ViolationImpl() {}

    public ViolationImpl(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
