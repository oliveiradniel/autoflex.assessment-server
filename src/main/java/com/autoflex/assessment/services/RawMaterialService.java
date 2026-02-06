package com.autoflex.assessment.services;

import com.autoflex.assessment.entities.RawMaterialEntity;
import com.autoflex.assessment.enums.UnitType;
import com.autoflex.assessment.exceptions.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RawMaterialService {

    public List<RawMaterialEntity> list() {
        return RawMaterialEntity.listAll();
    }

    public RawMaterialEntity findById(UUID id) {
        return (RawMaterialEntity) RawMaterialEntity.findByIdOptional(id)
                .orElseThrow(RawMaterialNotFoundException::new);
    }

    public RawMaterialEntity create(RawMaterialEntity rawMaterial) {
        if (RawMaterialEntity.existsByCode(rawMaterial.code)) {
            throw new CodeAlreadyInUseException();
        }

        if (RawMaterialEntity.existsByName(rawMaterial.name)) {
            throw new NameAlreadyInUseException();
        }

        RawMaterialEntity.persist(rawMaterial);

        return rawMaterial;
    }

    public RawMaterialEntity update(UUID id, RawMaterialEntity rawMaterial) {
        RawMaterialEntity entity = findById(id);

        if (rawMaterial.code != null && rawMaterial.code.length() > 20) {
            throw new BusinessException("Code must be at most 20 characters.", 422);
        }

        if (rawMaterial.stockQuantity != null && rawMaterial.stockQuantity < 0) {
            throw new BusinessException("Stock quantity cannot be negative.", 422);
        }

        if (rawMaterial.unitType != null && !UnitType.isValid(rawMaterial.unitType.name())) {
            throw new BusinessException("Invalid unit type", 422);
        }

        if (rawMaterial.code != null && !rawMaterial.code.equals(entity.code)) {
            if (RawMaterialEntity.existsByCode(rawMaterial.code)) {
                throw new CodeAlreadyInUseException();
            }
        }

        if (rawMaterial.name != null && !rawMaterial.name.equals(entity.name)) {
            if (RawMaterialEntity.existsByName(rawMaterial.name)) {
                throw new NameAlreadyInUseException();
            }
        }


        if (rawMaterial.stockQuantity != null) entity.stockQuantity = rawMaterial.stockQuantity;
        if (rawMaterial.unitType != null) entity.unitType = rawMaterial.unitType;
        if (rawMaterial.code != null) entity.code = rawMaterial.code;
        if (rawMaterial.name != null) entity.name = rawMaterial.name;

        RawMaterialEntity.persist(entity);

        return entity;
    }

    public void delete(UUID id) {
        findById(id);

        RawMaterialEntity.deleteById(id);
    }
}
