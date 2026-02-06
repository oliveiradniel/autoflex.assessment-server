package com.autoflex.assessment.enums;

import java.util.Arrays;

public enum UnitType {
    KG("Kilogram"),
    L("Litter"),
    UNIT("Unit");

    private final String description;

    UnitType(String description) {
        this.description = description;
    }

    public static boolean isValid(String value) {
        if (value == null) return false;

        return Arrays.stream(UnitType.values())
                .anyMatch(e -> e.name().equals(value));
    }

    public String getDescription() {
        return description;
    }
}
