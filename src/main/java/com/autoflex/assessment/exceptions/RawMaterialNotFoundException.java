package com.autoflex.assessment.exceptions;

public class RawMaterialNotFoundException extends BusinessException {

    public RawMaterialNotFoundException() { super("Raw material not found.", 404); }
}
