package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.rest.exception.IllegalRequestBodyException;
import com.exemple.products.demo.rest.handler.RestExceptionHandler;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - Test RestExceptionHandler")
class RestExceptionHandlerTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private BindingResult bindingResult;

    @Test
    @DisplayName("Must return a ResponseEntity object with status code 400")
    void shouldReturnBadRequestBodyIsInvalid() {
        var errors =
            List.of(new FieldError("Object test 1", "Field Test 1", "Default Message Test 1"));
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer());
        when(bindingResult.hasErrors()).thenReturn(Boolean.TRUE);
        when(bindingResult.getFieldErrors()).thenReturn(errors);

        var handler = new RestExceptionHandler();
        var exception = new IllegalRequestBodyException("Error", bindingResult);
        var response = handler.handleIllegalRequestBodyException(exception, httpServletRequest);

        var error = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals("[Field Test 1=Default Message Test 1]", error.getMessage());
    }

    @Test
    @DisplayName("Must return a ResponseEntity object with status code 400")
    void shouldReturnBadRequestWheRequestBody() {
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer());

        var handler = new RestExceptionHandler();
        var exception = Mockito.mock(HttpMessageNotReadableException.class);
        var response = handler.handleHttpMessageNotReadableException(exception, httpServletRequest);

        var error = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals("Request body is invalid", error.getMessage());
    }

    @Test
    @DisplayName("Must return a ResponseEntity object with status code 404")
    void shouldReturnNotFound() {
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer());

        var handler = new RestExceptionHandler();
        var exception = new EntityNotFoundException("Error");
        var response = handler.handlerEntityNotFoundException(exception, httpServletRequest);

        var error = response.getBody();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), error.getStatusCode());
        assertEquals("Error", error.getMessage());
    }
}
