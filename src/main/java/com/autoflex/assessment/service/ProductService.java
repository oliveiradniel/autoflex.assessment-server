package com.autoflex.assessment.service;

import com.autoflex.assessment.entity.ProductEntity;
import com.autoflex.assessment.entity.ProductMaterialEntity;
import com.autoflex.assessment.entity.RawMaterialEntity;
import com.autoflex.assessment.exception.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    private final RawMaterialService rawMaterialService;

    public ProductService(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    public List<ProductEntity> list() {
        return ProductEntity.listAll();
    }

    public ProductEntity findById(UUID id) {
        return (ProductEntity) ProductEntity.findByIdOptional(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public ProductEntity create(ProductEntity product) {
        if (ProductEntity.existsByCode(product.code)) {
            throw new CodeAlreadyInUseException();
        }

        if (ProductEntity.existsByName(product.name)) {
            throw new NameAlreadyInUseException();
        }

        ProductEntity createdProduct = new ProductEntity();

        createdProduct.code = product.code;
        createdProduct.name = product.name;
        createdProduct.price = product.price;
        createdProduct.description = product.description;

        for (ProductMaterialEntity material : product.getMaterials()) {
            RawMaterialEntity rawMaterial = rawMaterialService.findById(material.rawMaterial.id);

            createdProduct.addRawMaterial(rawMaterial, material.quantityNeeded);
        }

        createdProduct.persist();

        return createdProduct;
    }

    public ProductEntity update(UUID id, ProductEntity product) {
        ProductEntity existingProduct = findById(id);

        validateBasicFields(product, existingProduct);

        if (product.code != null) existingProduct.code = product.code;
        if (product.name != null) existingProduct.name = product.name;
        if (product.description != null) existingProduct.description = product.description;
        if (product.price != null) existingProduct.price = product.price;

        if (product.getMaterials() != null) {

            // Store all the raw material IDs of the shipped product
            Set<UUID> materialIds = product.getMaterials().stream()
                    .map(material -> {
                        if (material.rawMaterial == null || material.rawMaterial.id == null) {
                            throw new RawMaterialIdEmptyException();
                        }

                        return material.rawMaterial.id;
                    })
                    .collect(Collectors.toSet());

            // Go through all the raw materials sent to the association and make upserts
            for (ProductMaterialEntity material : product.getMaterials()) {

                RawMaterialEntity rawMaterial = rawMaterialService.findById(material.rawMaterial.id);

                existingProduct.upsertRawMaterial(rawMaterial, material.quantityNeeded);
            }

            // Store all the IDs of the materials that were not
            List<UUID> materialsToRemove = existingProduct.getMaterials().stream()
                    .map(productMaterial -> productMaterial.rawMaterial.id)
                    .filter(rawMaterialId -> !materialIds.contains(rawMaterialId))
                    .toList();

            // Remove materials that are in the database but were not included in the payload
            for (UUID removeId : materialsToRemove) {
                existingProduct.removeRawMaterial(removeId);
            }
        }

        existingProduct.persist();

        return existingProduct;
    }

    public void delete(UUID id) {
        ProductEntity product = findById(id);

        ProductEntity.deleteById(id);
    }

    private void validateBasicFields(ProductEntity product, ProductEntity existing) {

        if (product.code != null && product.code.length() > 20) {
            throw new BusinessException("Code must be at most 20 characters.", 422);
        }

        if (product.code != null && !product.code.equals(existing.code)
                && ProductEntity.existsByCode(product.code)) {
            throw new CodeAlreadyInUseException();
        }

        if (product.name != null && !product.name.equals(existing.name)
                && ProductEntity.existsByName(product.name)) {
            throw new NameAlreadyInUseException();
        }

        if (product.price != null
                && product.price.compareTo(new BigDecimal("0.01")) < 0) {
            throw new BusinessException("Price must be at least 0.01", 422);
        }

        if (product.description != null && product.description.length() > 500) {
            throw new BusinessException("Description must be at most 500 characters.", 422);
        }
    }


}
