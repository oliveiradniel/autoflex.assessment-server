package com.autoflex.assessment.dtos.mappers;

import com.autoflex.assessment.dtos.product.response.ProductResponse;
import com.autoflex.assessment.entities.ProductEntity;

public class ProductMapper {

    public static ProductResponse toResponse(ProductEntity product) {

        ProductResponse response = new ProductResponse();

        response.id = product.id;
        response.code = product.code;
        response.name = product.name;
        response.price = product.price;
        response.description = product.description;
        response.createdAt = product.createdAt;
        response.updatedAt = product.updatedAt;

        response.materials = product.getMaterials()
                .stream().map(material -> {
            ProductResponse.RawMaterialQuantity rawMaterial = new ProductResponse.RawMaterialQuantity();

            rawMaterial.rawMaterialId = material.rawMaterial.id;
            rawMaterial.rawMaterialName = material.rawMaterial.name;
            rawMaterial.quantityNeeded = material.quantityNeeded;

            return rawMaterial;
        }).toList();

        return response;
    }
}
