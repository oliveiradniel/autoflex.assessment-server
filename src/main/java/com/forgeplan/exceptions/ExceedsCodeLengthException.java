package com.forgeplan.exceptions;

public class ExceedsCodeLengthException extends BusinessException {

    public ExceedsCodeLengthException() {
        super("Code must be at most 20 characters.", 422);
    }
}
