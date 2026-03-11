package com.forgeplan.exceptions;

public class RawMaterialAlreadyExistsException extends BusinessException {

    public RawMaterialAlreadyExistsException() {
        super("This raw material is already part of the composition of this product.", 409);
    }
}
