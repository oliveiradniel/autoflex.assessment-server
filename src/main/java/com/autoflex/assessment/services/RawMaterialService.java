package com.autoflex.assessment.services;

import com.autoflex.assessment.dtos.mappers.RawMaterialMapper;
import com.autoflex.assessment.dtos.raw_material.request.RawMaterialCreateRequest;
import com.autoflex.assessment.dtos.raw_material.request.RawMaterialUpdateRequest;
import com.autoflex.assessment.dtos.raw_material.response.RawMaterialResponse;
import com.autoflex.assessment.entities.RawMaterialEntity;
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

        if (RawMaterialEntity.existsByCode(rawMaterial.getCode())) {
            throw new CodeAlreadyInUseException();
        }

        if (RawMaterialEntity.existsByName(rawMaterial.getName())) {
            throw new NameAlreadyInUseException();
        }

        RawMaterialEntity createdRawMaterial = new RawMaterialEntity();

        createdRawMaterial.setCode(rawMaterial.getCode());
        createdRawMaterial.setName(rawMaterial.getName());
        createdRawMaterial.setStockQuantity(rawMaterial.getStockQuantity());
        createdRawMaterial.setUnitType(rawMaterial.getUnitType());

        createdRawMaterial.persistAndFlush();

        return RawMaterialMapper.toResponse(createdRawMaterial);
    }

    public RawMaterialResponse update(UUID id, RawMaterialUpdateRequest rawMaterial) {

        RawMaterialEntity entity = findEntityById(id);

        if (rawMaterial.getCode() != null && rawMaterial.getCode().length() > 20) {
            throw new ExceedsCodeLengthException();
        }

        if (rawMaterial.getStockQuantity() == null || rawMaterial.getStockQuantity().compareTo(MIN_QUANTITY) < 0) {
            throw new MinimumStockQuantityException();
        }

        if (rawMaterial.getCode() != null && !rawMaterial.getCode().equals(entity.getCode())) {
            if (RawMaterialEntity.existsByCode(rawMaterial.getCode())) {
                throw new CodeAlreadyInUseException();
            }
        }

        if (rawMaterial.getName() != null && !rawMaterial.getName().equals(entity.getName())) {
            if (RawMaterialEntity.existsByName(rawMaterial.getName())) {
                throw new NameAlreadyInUseException();
            }
        }

        if (rawMaterial.getStockQuantity() != null) entity.setStockQuantity(rawMaterial.getStockQuantity());
        if (rawMaterial.getCode() != null) entity.setCode(rawMaterial.getCode());
        if (rawMaterial.getName() != null) entity.setName(rawMaterial.getName());

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
