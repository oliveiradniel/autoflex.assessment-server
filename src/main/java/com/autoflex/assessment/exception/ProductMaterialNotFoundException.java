package com.autoflex.assessment.exception;

public class ProductMaterialNotFoundException extends BusinessException {

    public ProductMaterialNotFoundException() { super("Product material not found.", 404); }
}
