package com.autoflex.assessment.exception;

public class RawMaterialIdEmptyException extends BusinessException {

    public RawMaterialIdEmptyException() {
        super("Raw material ID is required.", 409);
    }
}
