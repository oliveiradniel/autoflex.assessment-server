package com.forgeplan.exceptions;

public class RawMaterialNotFoundException extends BusinessException {

    public RawMaterialNotFoundException() { super("Raw material not found.", 404); }
}
