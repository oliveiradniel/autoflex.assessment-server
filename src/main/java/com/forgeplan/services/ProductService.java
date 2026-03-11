package com.forgeplan.services;

import com.forgeplan.dtos.mappers.ProductMapper;
import com.forgeplan.dtos.product.request.ProductCreateRequest;
import com.forgeplan.dtos.product.request.ProductUpdateRequest;
import com.forgeplan.dtos.product.response.ProductResponse;
import com.forgeplan.dtos.product.response.ProductSummaryResponse;
import com.forgeplan.dtos.product.response.ProductionReportResponse;
import com.forgeplan.dtos.raw_material.response.RawMaterialResponse;
import com.forgeplan.entities.ProductEntity;
import com.forgeplan.entities.RawMaterialEntity;
import com.forgeplan.exceptions.*;
import com.forgeplan.exceptions.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    private final RawMaterialService rawMaterialService;

    public ProductService(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    public List<ProductResponse> list() {

        return ProductEntity.listAll().stream()
                .map(entity -> ProductMapper.toResponse((ProductEntity) entity))
                .collect(Collectors.toList());
    }

    public ProductResponse findById(UUID id) {

        ProductEntity entity = (ProductEntity) ProductEntity.findByIdOptional(id)
                .orElseThrow(ProductNotFoundException::new);

        return ProductMapper.toResponse(entity);
    }

    public ProductResponse create(ProductCreateRequest product) {

        if (ProductEntity.existsByCode(product.getCode())) {
            throw new CodeAlreadyInUseException();
        }

        if (ProductEntity.existsByName(product.getName())) {
            throw new NameAlreadyInUseException();
        }

        ProductEntity createdProduct = new ProductEntity();

        createdProduct.setCode(product.getCode());
        createdProduct.setName(product.getName());
        createdProduct.setPrice(product.getPrice());
        createdProduct.setDescription(product.getDescription());

        if (product.getMaterials() != null) {

            for (ProductCreateRequest.RawMaterialQuantity material : product.getMaterials()) {
                RawMaterialEntity rawMaterial = rawMaterialService.findEntityById(material.getRawMaterialId());

                if (rawMaterial == null) {
                    throw new RawMaterialNotFoundException();
                }

                createdProduct.addRawMaterial(rawMaterial, material.getQuantityNeeded());
            }
        }

        createdProduct.persistAndFlush();

        return ProductMapper.toResponse(createdProduct);
    }

    public List<ProductionReportResponse> calculateProduction() {

        List<RawMaterialResponse> rawMaterials = rawMaterialService.list();

        Map<UUID, BigDecimal> virtualStock = new HashMap<>();
        Map<UUID, RawMaterialResponse> rawMaterialMap = new HashMap<>();

        for (RawMaterialResponse rawMaterial : rawMaterials) {
            virtualStock.put(rawMaterial.getId(), rawMaterial.getStockQuantity());
            rawMaterialMap.put(rawMaterial.getId(), rawMaterial);
        }

        List<ProductResponse> products = list();

        products.sort(
                Comparator.comparing(ProductResponse::getPrice).reversed()
        );

        List<ProductionReportResponse> productionReport = new ArrayList<>();

        for (ProductResponse product : products) {

            BigDecimal maxProducible = new BigDecimal(Integer.MAX_VALUE);
            boolean hasMaterials = false;

            for (ProductResponse.RawMaterialQuantity productMaterial : product.getRawMaterials()) {

                hasMaterials = true;

                BigDecimal stock = virtualStock.get(productMaterial.getRawMaterialId());
                BigDecimal quantityNeeded = productMaterial.getQuantityNeeded();

                if (stock == null || stock.compareTo(BigDecimal.ZERO) <= 0) {
                    maxProducible = BigDecimal.ZERO;
                    break;
                }

                BigDecimal possible = stock.divide(
                        quantityNeeded, 0, RoundingMode.DOWN
                );

                maxProducible = maxProducible.min(possible);
            }

            if (!hasMaterials || maxProducible.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            List<ProductionReportResponse.RawMaterial> rawMaterialInfos = new ArrayList<>();

            for (ProductResponse.RawMaterialQuantity productMaterial : product.getRawMaterials()) {

                UUID materialId = productMaterial.getRawMaterialId();

                BigDecimal stockBefore = virtualStock.get(materialId);
                BigDecimal consumed = productMaterial.getQuantityNeeded().multiply(maxProducible);
                BigDecimal stockAfter = stockBefore.subtract(consumed);

                RawMaterialResponse rawMaterial = rawMaterialMap.get(materialId);

                ProductionReportResponse.RawMaterial info =
                        new ProductionReportResponse.RawMaterial();

                info.setRawMaterialId(rawMaterial.getId());
                info.setRawMaterialCode(rawMaterial.getCode());
                info.setRawMaterialName(rawMaterial.getName());
                info.setRawMaterialUnitType(rawMaterial.getUnitType());
                info.setRequiredQuantity(productMaterial.getQuantityNeeded());
                info.setInitialStock(stockBefore);
                info.setConsumedQuantity(consumed);
                info.setRemainingStock(stockAfter);

                rawMaterialInfos.add(info);
            }

            for (ProductResponse.RawMaterialQuantity productMaterial : product.getRawMaterials()) {

                UUID materialId = productMaterial.getRawMaterialId();

                BigDecimal stock = virtualStock.get(materialId);
                BigDecimal consumed = productMaterial.getQuantityNeeded().multiply(maxProducible);

                virtualStock.put(materialId, stock.subtract(consumed));
            }

            ProductionReportResponse productReport = new ProductionReportResponse();

            productReport.setProductId(product.getId());
            productReport.setProductName(product.getName());
            productReport.setProductCode(product.getCode());
            productReport.setProduceQuantity(maxProducible.intValue());
            productReport.setTotalValue(product.getPrice().multiply(maxProducible));
            productReport.setRawMaterials(rawMaterialInfos);

            productionReport.add(productReport);
        }

        return productionReport;
    }

    public ProductResponse update(UUID id, ProductUpdateRequest product) {

        ProductEntity existingProduct = findEntityById(id);

        validateBasicFields(product, existingProduct);

        if (product.getCode() != null) existingProduct.setCode(product.getCode());
        if (product.getName() != null) existingProduct.setName(product.getName());
        if (product.getIsActive() != null) existingProduct.setIsActive(product.getIsActive());
        if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) existingProduct.setPrice(product.getPrice());

        if (product.getMaterials() != null) {

            // Store all the raw material IDs of the shipped product
            Set<UUID> materialIds = product.getMaterials().stream()
                    .map(material -> {
                        if (material.getRawMaterialId() == null) {
                            throw new RawMaterialIdEmptyException();
                        }

                        return material.getRawMaterialId();
                    })
                    .collect(Collectors.toSet());

            // Go through all the raw materials sent to the association and make upserts
            for (ProductUpdateRequest.RawMaterialQuantity material : product.getMaterials()) {

                RawMaterialEntity rawMaterial = rawMaterialService.findEntityById(material.getRawMaterialId());

                existingProduct.upsertRawMaterial(rawMaterial, material.getQuantityNeeded());
            }

            // Store all the IDs of the materials that were not
            List<UUID> materialsToRemove = existingProduct.getMaterials().stream()
                    .map(productMaterial -> productMaterial.getRawMaterial().getId())
                    .filter(rawMaterialId -> !materialIds.contains(rawMaterialId))
                    .toList();

            // Remove materials that are in the database but were not included in the payload
            for (UUID removeId : materialsToRemove) {
                existingProduct.removeRawMaterial(removeId);
            }
        }

        existingProduct.persist();

        return ProductMapper.toResponse(existingProduct);
    }

    public void delete(UUID id) {

        findById(id);

        ProductEntity.deleteById(id);
    }

    public ProductSummaryResponse getSummary() {
        return ProductEntity.getSummary();
    }

    private ProductEntity findEntityById(UUID id) {

        return (ProductEntity) ProductEntity.findByIdOptional(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    private void validateBasicFields(ProductUpdateRequest product, ProductEntity existingProduct) {

        if (product.getCode() != null && product.getCode().length() > 20) {
            throw new ExceedsCodeLengthException();
        }

        if (product.getCode() != null && !product.getCode().equals(existingProduct.getCode())
                && ProductEntity.existsByCode(product.getCode())) {
            throw new CodeAlreadyInUseException();
        }

        if (product.getName() != null && !product.getName().equals(existingProduct.getName())
                && ProductEntity.existsByName(product.getName())) {
            throw new NameAlreadyInUseException();
        }

        if (product.getPrice() != null
                && product.getPrice().compareTo(new BigDecimal("0.01")) < 0) {
            throw new MinimumPriceException();
        }

        if (product.getDescription() != null && product.getDescription().length() > 500) {
            throw new ExceedsDescriptionLengthException();
        }
    }
}
