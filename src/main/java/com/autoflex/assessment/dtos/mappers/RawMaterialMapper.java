package com.autoflex.assessment.dtos.mappers;

import com.autoflex.assessment.dtos.raw_material.response.RawMaterialResponse;
import com.autoflex.assessment.entities.RawMaterialEntity;

public class RawMaterialMapper {

    public static RawMaterialResponse toResponse(RawMaterialEntity rawMaterial) {

        RawMaterialResponse response = new RawMaterialResponse();

        response.id = rawMaterial.getId();
        response.code = rawMaterial.getCode();
        response.name = rawMaterial.getName();
        response.stockQuantity = rawMaterial.getStockQuantity();
        response.unitType = rawMaterial.getUnitType();
        response.createdAt = rawMaterial.getCreatedAt();
        response.updatedAt = rawMaterial.getUpdatedAt();

        return response;
    }
}
