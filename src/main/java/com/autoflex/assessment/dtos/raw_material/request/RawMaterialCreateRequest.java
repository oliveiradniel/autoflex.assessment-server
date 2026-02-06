package com.autoflex.assessment.dtos.raw_material.request;

import com.autoflex.assessment.enums.UnitType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public class RawMaterialCreateRequest {

    @Size(max = 20, message = "Code must be at most 20 characters.")
    @NotBlank(message = "Raw material code is required.")
    public String code;

    @NotBlank(message = "Raw material name is required.")
    public String name;

    @Min(value = 0, message = "Stock quantity cannot be negative.")
    @NotNull(message = "Stock quantity is required.")
    public Integer stockQuantity;

    @NotNull(message = "Unit type is required.")
    @Enumerated(EnumType.STRING)
    public UnitType unitType;
}