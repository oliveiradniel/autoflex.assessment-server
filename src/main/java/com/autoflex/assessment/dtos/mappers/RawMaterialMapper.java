package com.autoflex.assessment.dtos.mappers;

import com.autoflex.assessment.dtos.raw_material.response.RawMaterialResponse;
import com.autoflex.assessment.entities.RawMaterialEntity;

public class RawMaterialMapper {

    public static RawMaterialResponse toResponse(RawMaterialEntity rawMaterial) {

        RawMaterialResponse response = new RawMaterialResponse();

        response.setId(rawMaterial.getId());
        response.setCode(rawMaterial.getCode());
        response.setName(rawMaterial.getName());
        response.setStockQuantity(rawMaterial.getStockQuantity());
        response.setUnitType(rawMaterial.getUnitType());
        response.setCreatedAt(rawMaterial.getCreatedAt());
        response.setUpdatedAt(rawMaterial.getUpdatedAt());

        return response;
    }
}
