package com.autoflex.assessment.dtos.product.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductCreateRequest {

    @Size(max = 20, message = "Code must be at most 20 characters.")
    @NotBlank(message = "Product code is required.")
    public String code;

    @NotBlank(message = "Product name is required.")
    public String name;

    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    @NotNull(message = "Product price is required.")
    public BigDecimal price;

    @Size(max = 500, message = "Description must be at most 500 characters.")
    public String description;

    public List<RawMaterialQuantity> materials;

    public static class RawMaterialQuantity {

        @NotNull(message = "Raw material id is required.")
        public UUID rawMaterialId;

        @NotNull(message = "Quantity needed is required.")
        @DecimalMin(value = "0.01", message = "Quantity needed must be at least 0.01")
        public BigDecimal quantityNeeded;
    }
}
