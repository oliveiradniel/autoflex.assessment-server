package com.autoflex.assessment.dtos.mappers;

import com.autoflex.assessment.dtos.product.response.ProductResponse;
import com.autoflex.assessment.entities.ProductEntity;

public class ProductMapper {

    public static ProductResponse toResponse(ProductEntity product) {

        ProductResponse response = new ProductResponse();

        response.id = product.getId();
        response.code = product.getCode();
        response.name = product.getName();
        response.price = product.getPrice();
        response.isActive = product.getIsActive();
        response.description = product.getDescription();
        response.createdAt = product.getCreatedAt();
        response.updatedAt = product.getUpdatedAt();

        response.rawMaterials = product.getMaterials()
                .stream().map(material -> {
            ProductResponse.RawMaterialQuantity rawMaterial = new ProductResponse.RawMaterialQuantity();

            rawMaterial.rawMaterialId = material.getRawMaterial().getId();
            rawMaterial.rawMaterialName = material.getRawMaterial().getName();
            rawMaterial.quantityNeeded = material.getQuantityNeeded();
            rawMaterial.createdAt = material.getCreatedAt();
            rawMaterial.updatedAt = material.getUpdatedAt();

            return rawMaterial;
        }).toList();

        return response;
    }
}
