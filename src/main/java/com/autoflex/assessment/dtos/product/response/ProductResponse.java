package com.autoflex.assessment.dtos.product.response;

import com.autoflex.assessment.dtos.product.request.ProductCreateRequest;
import com.autoflex.assessment.dtos.product.request.ProductUpdateRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProductResponse {

    private UUID id;

    private String code;

    private String name;

    private BigDecimal price;

    private Boolean isActive;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<RawMaterialQuantity> rawMaterials;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<RawMaterialQuantity> getRawMaterials() { return rawMaterials; }
    public void setRawMaterials(List<RawMaterialQuantity> rawMaterials) { this.rawMaterials = rawMaterials; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class RawMaterialQuantity {

        private UUID rawMaterialId;

        private String rawMaterialName;

        private BigDecimal quantityNeeded;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public UUID getRawMaterialId() { return rawMaterialId; }
        public void setRawMaterialId(UUID rawMaterialId) { this.rawMaterialId = rawMaterialId; }

        public String getRawMaterialName() { return rawMaterialName; }
        public void setRawMaterialName(String rawMaterialName) { this.rawMaterialName = rawMaterialName; }

        public BigDecimal getQuantityNeeded() { return quantityNeeded; }
        public void setQuantityNeeded(BigDecimal quantityNeeded) { this.quantityNeeded = quantityNeeded; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }
}
