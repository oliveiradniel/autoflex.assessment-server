package com.autoflex.assessment.dtos.product.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductUpdateRequest {

    @Size(max = 20, message = "Code must be at most 20 characters.")
    private String code;

    private String name;

    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private BigDecimal price;

    private Boolean isActive;

    @Size(max = 500, message = "Description must be at most 500 characters.")
    private String description;

    private List<ProductUpdateRequest.RawMaterialQuantity> materials;

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

    public List<ProductUpdateRequest.RawMaterialQuantity> getMaterials() { return materials; }
    public void setMaterials(List<ProductUpdateRequest.RawMaterialQuantity> materials) { this.materials = materials; }

    public static class RawMaterialQuantity {

        @NotNull(message = "Raw material ID is required.")
        private UUID rawMaterialId;

        @NotNull(message = "Quantity needed is required.")
        @DecimalMin(value = "0.01", message = "Quantity needed must be at least 0.01")
        private BigDecimal quantityNeeded;

        public UUID getRawMaterialId() { return rawMaterialId; }
        public void setRawMaterialId(UUID rawMaterialId) { this.rawMaterialId = rawMaterialId; }

        public BigDecimal getQuantityNeeded() { return quantityNeeded; }
        public void setQuantityNeeded(BigDecimal quantityNeeded) { this.quantityNeeded = quantityNeeded; }
    }
}
