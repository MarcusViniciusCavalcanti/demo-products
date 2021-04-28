package com.exemple.products.demo.domain.product.exception;

public class ProductNameException extends RuntimeException {

    public ProductNameException(String name) {
        super(String.format("this name [%s] is exist in other product", name));
    }
}
