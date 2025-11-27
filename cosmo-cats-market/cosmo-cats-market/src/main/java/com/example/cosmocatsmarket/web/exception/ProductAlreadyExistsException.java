package com.example.cosmocatsmarket.web.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Product with name '%s' already exists";

    public ProductAlreadyExistsException(String productName) {
        super(String.format(MESSAGE_TEMPLATE, productName));
    }
}
