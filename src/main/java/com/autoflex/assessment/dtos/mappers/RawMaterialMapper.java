package com.autoflex.assessment.dtos.mappers;

import com.autoflex.assessment.dtos.raw_material.response.RawMaterialResponse;
import com.autoflex.assessment.entities.RawMaterialEntity;

public class RawMaterialMapper {

    public static RawMaterialResponse toResponse(RawMaterialEntity rawMaterial) {

        RawMaterialResponse response = new RawMaterialResponse();

        response.id = rawMaterial.id;
        response.code = rawMaterial.code;
        response.name = rawMaterial.name;
        response.stockQuantity = rawMaterial.stockQuantity;
        response.unitType = rawMaterial.unitType;
        response.createdAt = rawMaterial.createdAt;
        response.updatedAt = rawMaterial.updatedAt;

        return response;
    }
}
