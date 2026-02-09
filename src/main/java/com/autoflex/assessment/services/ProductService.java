package com.autoflex.assessment.services;

import com.autoflex.assessment.dtos.mappers.ProductMapper;
import com.autoflex.assessment.dtos.product.request.ProductCreateRequest;
import com.autoflex.assessment.dtos.product.request.ProductUpdateRequest;
import com.autoflex.assessment.dtos.product.response.ProductResponse;
import com.autoflex.assessment.dtos.product.response.ProductSummaryResponse;
import com.autoflex.assessment.dtos.product.response.ProductionReportResponse;
import com.autoflex.assessment.dtos.raw_material.response.RawMaterialResponse;
import com.autoflex.assessment.entities.ProductEntity;
import com.autoflex.assessment.entities.RawMaterialEntity;
import com.autoflex.assessment.exceptions.*;
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

        for (ProductCreateRequest.RawMaterialQuantity material : product.materials) {
            RawMaterialEntity rawMaterial = rawMaterialService.findEntityById(material.rawMaterialId);

            createdProduct.addRawMaterial(rawMaterial, material.quantityNeeded);
        }

        createdProduct.persistAndFlush();

        return ProductMapper.toResponse(createdProduct);
    }

    public List<ProductionReportResponse> calculateProduction() {

        List<RawMaterialResponse> rawMaterials = rawMaterialService.list();

        Map<UUID, BigDecimal> virtualStock = new HashMap<>();
        Map<UUID, RawMaterialResponse> rawMaterialMap = new HashMap<>();

        for (RawMaterialResponse rawMaterial : rawMaterials) {
            virtualStock.put(rawMaterial.id, rawMaterial.stockQuantity);
            rawMaterialMap.put(rawMaterial.id, rawMaterial);
        }

        List<ProductResponse> products = list();

        products.sort(
                Comparator.comparing((ProductResponse p) -> p.id).reversed()
        );

        List<ProductionReportResponse> productionReport = new ArrayList<>();

        for (ProductResponse product : products) {

            BigDecimal maxProducible = new BigDecimal(Integer.MAX_VALUE);
            boolean hasMaterials = false;

            for (ProductResponse.RawMaterialQuantity productMaterial : product.rawMaterials) {

                hasMaterials = true;

                BigDecimal stock = virtualStock.get(productMaterial.rawMaterialId);
                BigDecimal quantityNeeded = productMaterial.quantityNeeded;

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

            List<ProductionReportResponse.RawMaterialInfo> rawMaterialInfos = new ArrayList<>();

            for (ProductResponse.RawMaterialQuantity productMaterial : product.rawMaterials) {

                UUID materialId = productMaterial.rawMaterialId;

                BigDecimal stockBefore = virtualStock.get(materialId);
                BigDecimal consumed = productMaterial.quantityNeeded.multiply(maxProducible);
                BigDecimal stockAfter = stockBefore.subtract(consumed);

                RawMaterialResponse rawMaterial = rawMaterialMap.get(materialId);

                ProductionReportResponse.RawMaterialInfo info =
                        new ProductionReportResponse.RawMaterialInfo();

                info.materialId = rawMaterial.id;
                info.materialCode = rawMaterial.code;
                info.materialName = rawMaterial.name;
                info.materialUnitType = rawMaterial.unitType;
                info.requiredQuantity = productMaterial.quantityNeeded;
                info.initialStock = stockBefore;
                info.consumedQuantity = consumed;
                info.remainingStock = stockAfter;

                rawMaterialInfos.add(info);
            }

            for (ProductResponse.RawMaterialQuantity productMaterial : product.rawMaterials) {

                UUID materialId = productMaterial.rawMaterialId;

                BigDecimal stock = virtualStock.get(materialId);
                BigDecimal consumed = productMaterial.quantityNeeded.multiply(maxProducible);

                virtualStock.put(materialId, stock.subtract(consumed));
            }

            ProductionReportResponse productReport = new ProductionReportResponse();

            productReport.productId = product.id;
            productReport.productName = product.name;
            productReport.productCode = product.code;
            productReport.produceQuantity = maxProducible.intValue();
            productReport.totalValue =
                    product.price.multiply(maxProducible).intValue();
            productReport.rawMaterials = rawMaterialInfos;

            productionReport.add(productReport);
        }

        return productionReport;
    }

    public ProductResponse update(UUID id, ProductUpdateRequest product) {

        ProductEntity existingProduct = findEntityById(id);

        validateBasicFields(product, existingProduct);

        if (product.code != null) existingProduct.code = product.code;
        if (product.name != null) existingProduct.name = product.name;
        if (product.isActive != null) existingProduct.isActive = product.isActive;
        if (product.description != null) existingProduct.description = product.description;
        if (product.price != null) existingProduct.price = product.price;

        if (product.materials != null) {

            // Store all the raw material IDs of the shipped product
            Set<UUID> materialIds = product.materials.stream()
                    .map(material -> {
                        if (material.rawMaterialId == null) {
                            throw new RawMaterialIdEmptyException();
                        }

                        return material.rawMaterialId;
                    })
                    .collect(Collectors.toSet());

            // Go through all the raw materials sent to the association and make upserts
            for (ProductUpdateRequest.RawMaterialQuantity material : product.materials) {

                RawMaterialEntity rawMaterial = rawMaterialService.findEntityById(material.rawMaterialId);

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

        return ProductMapper.toResponse(existingProduct);
    }

    public void delete(UUID id) {

        ProductResponse product = findById(id);

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

        if (product.code != null && product.code.length() > 20) {
            throw new BusinessException("Code must be at most 20 characters.", 422);
        }

        if (product.code != null && !product.code.equals(existingProduct.code)
                && ProductEntity.existsByCode(product.code)) {
            throw new CodeAlreadyInUseException();
        }

        if (product.name != null && !product.name.equals(existingProduct.name)
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
