package com.exemple.products.demo.rest.handler;

import com.exemple.products.demo.domain.product.exception.ProductNameException;
import com.exemple.products.demo.rest.errors.ResponseError;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleIllegalRequestBodyException(
        MethodArgumentNotValidException exception, HttpServletRequest request) {
        return buildBadRequest(exception, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseError> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException exception, HttpServletRequest request) {
        return buildBadRequest(exception, request);
    }

    @ExceptionHandler(ProductNameException.class)
    public ResponseEntity<ResponseError> handleProductNameException(ProductNameException exception,
        HttpServletRequest request) {
        return buildResponseErrorBy(
            "Name product is exist other Product",
            request,
            exception,
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseError> handlerEntityNotFoundException(
        EntityNotFoundException exception,
        HttpServletRequest request) {
        log.error(
            String.format("Error in process request: %s cause by: %s", request.getRequestURL(),
                exception.getClass().getSimpleName()));
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<ResponseError> buildBadRequest(Exception exception,
        HttpServletRequest request) {
        return buildResponseErrorBy(
            "Request body is invalid",
            request,
            exception,
            HttpStatus.BAD_REQUEST
        );
    }

    private ResponseEntity<ResponseError> buildResponseErrorBy(String msg,
        HttpServletRequest request, Exception exception, HttpStatus status) {
        log.error(
            String.format("Error in process request: %s cause by: %s", request.getRequestURL(),
                exception.getClass().getSimpleName()));
        var errors = ResponseError.builder()
            .message(msg)
            .statusCode(status.value())
            .build();

        return new ResponseEntity<>(errors, status);
    }
}
