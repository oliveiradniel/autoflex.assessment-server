package com.autoflex.assessment.dtos.product.response;

import com.autoflex.assessment.enums.UnitType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductionReportResponse {

    private UUID productId;

    private String productCode;

    private String productName;

    private Integer produceQuantity;

    private BigDecimal totalValue;

    private List<RawMaterial> rawMaterials;

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getProduceQuantity() { return produceQuantity; }
    public void setProduceQuantity(Integer produceQuantity) { this.produceQuantity = produceQuantity; }

    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }

    public List<RawMaterial> getRawMaterials() { return rawMaterials; }
    public void setRawMaterials(List<RawMaterial> rawMaterials) { this.rawMaterials = rawMaterials; }

    public static class RawMaterial {

        private UUID rawMaterialId;

        private String rawMaterialCode;

        private String rawMaterialName;

        private UnitType rawMaterialUnitType;

        private BigDecimal requiredQuantity;

        private BigDecimal initialStock;

        private BigDecimal consumedQuantity;

        private BigDecimal remainingStock;

        public UUID getRawMaterialId() { return rawMaterialId; }
        public void setRawMaterialId(UUID rawMaterialId) { this.rawMaterialId = rawMaterialId; }

        public String getRawMaterialCode() { return rawMaterialCode; }
        public void setRawMaterialCode(String rawMaterialCode) { this.rawMaterialCode = rawMaterialCode; }

        public String getRawMaterialName() { return rawMaterialName; }
        public void setRawMaterialName(String rawMaterialName) { this.rawMaterialName = rawMaterialName; }

        public UnitType getRawMaterialUnitType() { return rawMaterialUnitType; }
        public void setRawMaterialUnitType(UnitType rawMaterialUnitType) { this.rawMaterialUnitType = rawMaterialUnitType; }

        public BigDecimal getRequiredQuantity() { return requiredQuantity; }
        public void setRequiredQuantity(BigDecimal requiredQuantity) { this.requiredQuantity = requiredQuantity; }

        public BigDecimal getInitialStock() { return initialStock; }
        public void setInitialStock(BigDecimal initialStock) { this.initialStock = initialStock; }

        public BigDecimal getConsumedQuantity() { return consumedQuantity; }
        public void setConsumedQuantity(BigDecimal consumedQuantity) { this.consumedQuantity = consumedQuantity; }

        public BigDecimal getRemainingStock() { return remainingStock; }
        public void setRemainingStock(BigDecimal remainingStock) { this.remainingStock = remainingStock; }
    }
}
