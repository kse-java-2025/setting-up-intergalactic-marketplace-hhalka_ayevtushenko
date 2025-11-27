package com.example.cosmocatsmarket.web.exception;


public class ProductNotFoundException extends RuntimeException {
    public static final String MESSAGE = "Product with id '%s' not found";
    public ProductNotFoundException(String id) {
        super(String.format(MESSAGE, id));
    }
}
