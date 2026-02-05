package com.autoflex.assessment.exception;

public class CodeAlreadyInUseException extends BusinessException {

    public CodeAlreadyInUseException() {
        super("This code already in use.", 409);
    }
}
