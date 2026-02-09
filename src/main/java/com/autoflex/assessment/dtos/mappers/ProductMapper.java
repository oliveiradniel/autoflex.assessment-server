package com.autoflex.assessment.dtos.mappers;

import com.autoflex.assessment.dtos.product.response.ProductResponse;
import com.autoflex.assessment.entities.ProductEntity;

public class ProductMapper {

    public static ProductResponse toResponse(ProductEntity product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setCode(product.getCode());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setIsActive(product.getIsActive());
        response.setDescription(product.getDescription());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        response.setRawMaterials(product.getMaterials()
                .stream().map(material -> {
                    ProductResponse.RawMaterialQuantity rawMaterial = new ProductResponse.RawMaterialQuantity();

                    rawMaterial.setRawMaterialId(material.getRawMaterial().getId());
                    rawMaterial.setRawMaterialName(material.getRawMaterial().getName());
                    rawMaterial.setQuantityNeeded(material.getQuantityNeeded());
                    rawMaterial.setCreatedAt(material.getCreatedAt());
                    rawMaterial.setUpdatedAt(material.getUpdatedAt());

                    return rawMaterial;
                }).toList());

        return response;
    }
}
