package com.autoflex.assessment.exceptions;

public class ProductMaterialNotFoundException extends BusinessException {

    public ProductMaterialNotFoundException() { super("Product material not found.", 404); }
}
