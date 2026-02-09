package com.autoflex.assessment.dtos.raw_material.request;

import com.autoflex.assessment.enums.UnitType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class RawMaterialUpdateRequest {

    @Size(max = 20, message = "Code must be at most 20 characters.")
    public String code;

    public String name;

    @DecimalMin(value = "0.01", message = "Stock must be at least 0.01.")
    public BigDecimal stockQuantity;

    @Enumerated(EnumType.STRING)
    public UnitType unitType;
}
