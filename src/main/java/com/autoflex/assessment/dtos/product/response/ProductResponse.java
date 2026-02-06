package com.autoflex.assessment.dtos.product.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProductResponse {

    public UUID id;

    public String code;

    public String name;

    public BigDecimal price;

    public String description;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    public List<RawMaterialQuantity> materials;

    public static class RawMaterialQuantity {

        public UUID rawMaterialId;

        public String rawMaterialName;

        public BigDecimal quantityNeeded;
    }
}
