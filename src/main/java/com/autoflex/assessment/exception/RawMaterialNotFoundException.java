package com.autoflex.assessment.exception;

public class RawMaterialNotFoundException extends BusinessException {

    public RawMaterialNotFoundException() { super("Raw material not found.", 404); }
}
