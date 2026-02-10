package com.autoflex.assessment.exceptions;

public class ExceedsDescriptionLengthException extends BusinessException {

    public ExceedsDescriptionLengthException() {
        super("Description must be at most 500 characters.", 422);
    }
}
