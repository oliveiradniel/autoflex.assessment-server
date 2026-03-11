package com.forgeplan.exceptions;

public class EmptyJsonException extends BusinessException {

    public EmptyJsonException() {
        super("The request body cannot be empty.", 422);
    }
}
