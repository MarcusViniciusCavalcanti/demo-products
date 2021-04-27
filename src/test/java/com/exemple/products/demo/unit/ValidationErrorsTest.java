package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.exemple.products.demo.rest.errors.ValidationErrors;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;

@DisplayName("Unit Test - ValidationErrors")
class ValidationErrorsTest {

    // TODO #1 rever
    @Test
    @DisplayName("Should return message error formatted this pattern [%s=%s] separate with '-'")
    void shouldReturnMessageErrorFormatted() {
        var error1 = new FieldError("Object test 1", "Field Test 1", "Default Message Test 1");
        var error2 = new FieldError("Object test 2", "Field Test 2", "Default Message Test 2");
        var validationErrors = new ValidationErrors(List.of(error1, error2));

        var errorMessage = validationErrors.getErrorMessage();

        assertEquals("[Field Test 1=Default Message Test 1] - [Field Test 2=Default Message Test 2]", errorMessage);
    }
}
