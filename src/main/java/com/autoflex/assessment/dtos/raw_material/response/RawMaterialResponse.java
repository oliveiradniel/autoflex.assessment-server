package com.autoflex.assessment.dtos.raw_material.response;

import com.autoflex.assessment.enums.UnitType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class RawMaterialResponse {

  private UUID id;

  private String code;

  private String name;

  private BigDecimal stockQuantity;

  private UnitType unitType;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(BigDecimal stockQuantity) { this.stockQuantity = stockQuantity; }

    public UnitType getUnitType() { return unitType; }
    public void setUnitType(UnitType unitType) { this.unitType = unitType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
