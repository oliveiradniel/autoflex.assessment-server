package com.autoflex.assessment.dtos.raw_material.response;

import com.autoflex.assessment.enums.UnitType;

import java.time.LocalDateTime;
import java.util.UUID;

public class RawMaterialResponse {

    public UUID id;

    public String code;

    public String name;

    public Integer stockQuantity;

    public UnitType unitType;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;
}
