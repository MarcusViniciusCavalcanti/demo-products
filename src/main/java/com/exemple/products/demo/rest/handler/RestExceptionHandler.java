package com.exemple.products.demo.rest.handler;

import com.exemple.products.demo.rest.errors.ResponseError;
import com.exemple.products.demo.rest.errors.ValidationErrors;
import com.exemple.products.demo.rest.exception.IllegalRequestBodyException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(IllegalRequestBodyException.class)
    public ResponseEntity<ResponseError> handleIllegalRequestBodyException(
        IllegalRequestBodyException exception, HttpServletRequest request) {
        return buildResponseErrorBy(
            new ValidationErrors(exception.errors()).getErrorMessage(),
            request,
            exception,
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseError> handlerEntityNotFoundException(EntityNotFoundException exception,
        HttpServletRequest request) {
        return buildResponseErrorBy(exception.getMessage(), request, exception, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ResponseError> buildResponseErrorBy(String msg,
        HttpServletRequest request, Exception exception, HttpStatus status) {
        log.error(String
            .format("Error in process request: %s cause by: %s", request.getRequestURL(),
                exception.getClass().getSimpleName()));
        var errors = ResponseError.builder()
            .message(msg)
            .statusCode(status.value())
            .build();

        return new ResponseEntity<>(errors, status);
    }
}
