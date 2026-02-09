package com.autoflex.assessment.dtos.raw_material.response;

import com.autoflex.assessment.enums.UnitType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class RawMaterialResponse {

    public UUID id;

    public String code;

    public String name;

    public BigDecimal stockQuantity;

    public UnitType unitType;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;
}
