package com.autoflex.assessment.dtos.raw_material.request;

import com.autoflex.assessment.enums.UnitType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class RawMaterialCreateRequest {

    @Size(max = 20, message = "Code must be at most 20 characters.")
    @NotBlank(message = "Raw material code is required.")
    private String code;

    @NotBlank(message = "Raw material name is required.")
    private String name;

    @DecimalMin(value = "0.01", message = "Stock must be at least 0.01.")
    @NotNull(message = "Stock quantity is required.")
    private BigDecimal stockQuantity;

    @NotNull(message = "Unit type is required.")
    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(BigDecimal stockQuantity) { this.stockQuantity = stockQuantity; }

    public UnitType getUnitType() { return unitType; }
    public void setUnitType(UnitType unitType) { this.unitType = unitType; }
}