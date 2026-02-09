package com.autoflex.assessment.dtos.product.response;

import com.autoflex.assessment.enums.UnitType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductionReportResponse {

    public UUID productId;

    public String productCode;

    public String productName;

    public Integer produceQuantity;

    public Integer totalValue;

    public List<RawMaterialInfo> rawMaterials;

    public static class RawMaterialInfo {

        public UUID materialId;

        public String materialCode;

        public String materialName;

        public UnitType materialUnitType;

        public BigDecimal requiredQuantity;

        public BigDecimal initialStock;

        public BigDecimal consumedQuantity;

        public BigDecimal remainingStock;
    }
}
