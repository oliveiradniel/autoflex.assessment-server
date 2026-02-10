package com.autoflex.assessment.exceptions;

public class MinimumStockQuantityException extends BusinessException {

    public MinimumStockQuantityException() {
        super("Stock quantity needed must be at least 0.01", 422);
    }
}
