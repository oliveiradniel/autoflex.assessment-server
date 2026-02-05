package com.autoflex.assessment.exception;

public class NameAlreadyInUseException extends BusinessException {

    public NameAlreadyInUseException() {
        super("This name already in use.", 409);
    }
}
