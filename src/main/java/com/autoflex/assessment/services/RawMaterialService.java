package com.autoflex.assessment.services;

import com.autoflex.assessment.dtos.mappers.RawMaterialMapper;
import com.autoflex.assessment.dtos.raw_material.request.RawMaterialCreateRequest;
import com.autoflex.assessment.dtos.raw_material.request.RawMaterialUpdateRequest;
import com.autoflex.assessment.dtos.raw_material.response.RawMaterialResponse;
import com.autoflex.assessment.entities.RawMaterialEntity;
import com.autoflex.assessment.enums.UnitType;
import com.autoflex.assessment.exceptions.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RawMaterialService {

    private static final BigDecimal MIN_QUANTITY = new BigDecimal("0.01");

    public List<RawMaterialResponse> list() {

        return RawMaterialEntity.listAll().stream()
                .map(entity -> RawMaterialMapper.toResponse((RawMaterialEntity) entity))
                .collect(Collectors.toList());
    }

    public RawMaterialResponse findById(UUID id) {

        RawMaterialEntity entity = (RawMaterialEntity) RawMaterialEntity.findByIdOptional(id)
                .orElseThrow(RawMaterialNotFoundException::new);

        return RawMaterialMapper.toResponse(entity);
    }

    public RawMaterialResponse create(RawMaterialCreateRequest rawMaterial) {

        if (RawMaterialEntity.existsByCode(rawMaterial.code)) {
            throw new CodeAlreadyInUseException();
        }

        if (RawMaterialEntity.existsByName(rawMaterial.name)) {
            throw new NameAlreadyInUseException();
        }

        RawMaterialEntity createdRawMaterial = new RawMaterialEntity();

        createdRawMaterial.code = rawMaterial.code;
        createdRawMaterial.name = rawMaterial.name;
        createdRawMaterial.stockQuantity = rawMaterial.stockQuantity;
        createdRawMaterial.unitType = rawMaterial.unitType;

        createdRawMaterial.persistAndFlush();

        return RawMaterialMapper.toResponse(createdRawMaterial);
    }

    public RawMaterialResponse update(UUID id, RawMaterialUpdateRequest rawMaterial) {

        RawMaterialEntity entity = findEntityById(id);

        if (rawMaterial.code != null && rawMaterial.code.length() > 20) {
            throw new BusinessException("Code must be at most 20 characters.", 422);
        }

        if (rawMaterial.stockQuantity == null || rawMaterial.stockQuantity.compareTo(MIN_QUANTITY) < 0) {
            throw new BusinessException("Stock quantity needed must be at least 0.01", 422);
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

        return RawMaterialMapper.toResponse(entity);
    }

    public void delete(UUID id) {

        findById(id);

        RawMaterialEntity.deleteById(id);
    }

    public RawMaterialEntity findEntityById(UUID id) {

        return (RawMaterialEntity) RawMaterialEntity.findByIdOptional(id)
                .orElseThrow(RawMaterialNotFoundException::new);
    }
}
