package com.autoflex.assessment.exceptions;

public class MinimumQuantityNeededException extends BusinessException {

    public MinimumQuantityNeededException() {
        super("Quantity needed must be at least 0.01.", 422);
    }
}
