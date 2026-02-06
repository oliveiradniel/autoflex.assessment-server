package com.autoflex.assessment.exceptions;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() { super("Product not found.", 404); }
}
