package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.domain.product.exception.ProductNameException;
import com.exemple.products.demo.rest.handler.RestExceptionHandler;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - Test RestExceptionHandler")
class RestExceptionHandlerTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    @DisplayName("Must return a ResponseEntity object with status code 400")
    void shouldReturnBadRequestBodyIsInvalid() {
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer());

        var exception = Mockito.mock(MethodArgumentNotValidException.class);

        var handler = new RestExceptionHandler();
        var response = handler.handleIllegalRequestBodyException(exception, httpServletRequest);

        var error = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals("Request body is invalid", error.getMessage());
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

    @Test
    @DisplayName("Must return a ResponseEntity object with status code 400 when ProductNameException")
    void shouldReturnProductNameException() {
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer());

        var handler = new RestExceptionHandler();
        var exception = new ProductNameException("Error");
        var response = handler.handleProductNameException(exception, httpServletRequest);

        var error = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals("Name product is exist other Product", error.getMessage());
    }
}
