package com.exemple.products.demo.rest.errors;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.validation.FieldError;

@Data
public class ValidationErrors implements Serializable {

    private List<Field> fieldErrors;

    public ValidationErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors.stream().map(Field::new).collect(Collectors.toList());
    }

    public String getErrorMessage() {
        return fieldErrors.stream()
            .map(field -> {
                var message = field.message;
                var filedName = field.field;

                return String.format("[%s=%s]", filedName, message);
            })
            .collect(Collectors.joining(" - "));
    }

    @Data
    public static class Field {

        private String message;
        private String field;

        public Field(FieldError objectError) {
            this.field = objectError.getField();
            this.message = objectError.getDefaultMessage();
        }
    }
}
