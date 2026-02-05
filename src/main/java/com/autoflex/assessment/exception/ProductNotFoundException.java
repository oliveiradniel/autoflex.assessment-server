package com.autoflex.assessment.exception;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() { super("Product not found.", 404); }
}
