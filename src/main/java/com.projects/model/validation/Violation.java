package com.projects.model.validation;

public interface Violation {
    void setField(String field);

    String getField();

    void setMessage(String message);

    String getMessage();
}
