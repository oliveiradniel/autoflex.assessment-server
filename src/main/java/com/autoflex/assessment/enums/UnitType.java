package com.autoflex.assessment.enums;

public enum UnitType {
    KG("Kilogram"),
    L("Litter"),
    UNIT("Unit");

    private final String description;

    UnitType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
