package com.autoflex.assessment.exceptions;

public class CodeAlreadyInUseException extends BusinessException {

    public CodeAlreadyInUseException() {
        super("This code already in use.", 409);
    }
}
