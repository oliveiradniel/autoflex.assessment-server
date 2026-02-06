package com.autoflex.assessment.exceptions;

public class NameAlreadyInUseException extends BusinessException {

    public NameAlreadyInUseException() {
        super("This name already in use.", 409);
    }
}
