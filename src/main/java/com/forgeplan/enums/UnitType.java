package com.forgeplan.enums;

import java.util.Arrays;

public enum UnitType {
    KG("Kilogram"),         // large weighable solids
    G("Gram"),              // small solids
    L("Litre"),             // liquids
    ML("Millilitre"),       // small liquids
    UNIT("Unit"),           // individual pieces
    PACK("Pack"),           // packages or sets
    BOX("Box"),             // boxes
    ROLL("Roll"),           // rolls, fabrics, tapes
    SHEET("Sheet"),         // sheets, plates
    M("Meter"),             // materials in length
    CM("Centimeter");       // precision in length

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
