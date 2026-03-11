package com.forgeplan.exceptions;

public class NameAlreadyInUseException extends BusinessException {

    public NameAlreadyInUseException() {
        super("This name already in use.", 409);
    }
}
