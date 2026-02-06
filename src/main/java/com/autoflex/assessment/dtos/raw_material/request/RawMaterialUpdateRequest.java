package com.autoflex.assessment.dtos.raw_material.request;

import com.autoflex.assessment.enums.UnitType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public class RawMaterialUpdateRequest {

    @Size(max = 20, message = "Code must be at most 20 characters.")
    public String code;

    public String name;

    @Min(value = 0, message = "Stock quantity cannot be negative.")
    public Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    public UnitType unitType;
}
