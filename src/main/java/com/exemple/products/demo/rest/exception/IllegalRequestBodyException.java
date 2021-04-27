package com.exemple.products.demo.rest.exception;

import java.util.Collections;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class IllegalRequestBodyException extends RuntimeException {
    private final BindingResult resultSet;

    public IllegalRequestBodyException(String msg, BindingResult resultSet) {
        super(msg);
        this.resultSet = resultSet;
    }

    public List<FieldError> errors() {
        if (resultSet.hasErrors()) {
            return resultSet.getFieldErrors();
        }
        return Collections.emptyList();
    }
}
