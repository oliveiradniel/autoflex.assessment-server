package com.forgeplan.exceptions;

public class RawMaterialIdEmptyException extends BusinessException {

    public RawMaterialIdEmptyException() {
        super("Raw material ID is required.", 409);
    }
}
