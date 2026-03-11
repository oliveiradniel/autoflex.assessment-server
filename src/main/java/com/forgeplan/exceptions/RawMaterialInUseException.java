package com.forgeplan.exceptions;

public class RawMaterialInUseException extends BusinessException {

    public RawMaterialInUseException() {
        super("Cannot delete: this material is associated with one or more products.", 409);
    }
}
