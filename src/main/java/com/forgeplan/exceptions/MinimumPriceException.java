package com.forgeplan.exceptions;

public class MinimumPriceException extends BusinessException {

    public MinimumPriceException() {
        super("Price must be at least 0.01.", 422);
    }
}
