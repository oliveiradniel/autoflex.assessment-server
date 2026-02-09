package com.autoflex.assessment.dtos.raw_material.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class RawMaterialUpdateRequest {

    @Size(max = 20, message = "Code must be at most 20 characters.")
    private String code;

    private String name;

    @DecimalMin(value = "0.01", message = "Stock must be at least 0.01.")
    private BigDecimal stockQuantity;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(BigDecimal stockQuantity) { this.stockQuantity = stockQuantity; }
}
