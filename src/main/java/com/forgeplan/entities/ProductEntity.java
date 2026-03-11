package com.forgeplan.entities;

import com.forgeplan.dtos.product.response.ProductSummaryResponse;
import com.forgeplan.exceptions.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.forgeplan.exceptions.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_products")
public class ProductEntity extends PanacheEntityBase {

    private static final BigDecimal MIN_QUANTITY = new BigDecimal("0.01");

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, length = 20, nullable = false)
    @Size(max = 20, message = "Code must be at most 20 characters.")
    @NotBlank(message = "Product code is required.")
    private String code;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Product name is required.")
    private String name;

    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01.")
    @NotNull(message = "Product price is required.")
    private BigDecimal price;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Column(length = 500, nullable = true)
    @Size(max = 500, message = "Description must be at most 500 characters.")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false,
            columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private final List<ProductMaterialEntity> materials = new ArrayList<>();

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<ProductMaterialEntity> getMaterials() {
        return Collections.unmodifiableList(materials);
    }

    public static boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    public static boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public void addRawMaterial(
            RawMaterialEntity rawMaterial, BigDecimal quantityNeeded
    ) {

        if (rawMaterial == null || rawMaterial.getId() == null) {
            throw new BusinessException("Raw material is required", 422);
        }

        if (quantityNeeded == null || quantityNeeded.compareTo(MIN_QUANTITY) < 0) {
            throw new MinimumQuantityNeededException();
        }

        boolean alreadyExists = materials.stream()
                .anyMatch(productMaterial -> productMaterial.getRawMaterial() != null
                        && productMaterial.getRawMaterial().getId().equals(rawMaterial.getId()));

        if (alreadyExists) {
            throw new RawMaterialAlreadyExistsException();
        }

        ProductMaterialEntity productMaterial = new ProductMaterialEntity();

        productMaterial.setProduct(this);
        productMaterial.setRawMaterial(rawMaterial);
        productMaterial.setQuantityNeeded(quantityNeeded);

        materials.add(productMaterial);
    }

    public void upsertRawMaterial(RawMaterialEntity rawMaterial, BigDecimal quantity) {

        if (rawMaterial == null || rawMaterial.getId() == null) {
            throw new RawMaterialIdEmptyException();
        }

        if (quantity == null || quantity.compareTo(MIN_QUANTITY) < 0) {
            throw new MinimumQuantityNeededException();
        }

        ProductMaterialEntity found = materials.stream()
                .filter(productMaterial -> productMaterial.getRawMaterial() != null
                        && productMaterial.getRawMaterial().getId().equals(rawMaterial.getId()))
                .findFirst()
                .orElse(null);

        if (found != null) {
            found.setQuantityNeeded(quantity);
            return;
        }

        addRawMaterial(rawMaterial, quantity);
    }

    public void removeRawMaterial(UUID rawMaterialId) {

        if (rawMaterialId == null) {
            throw new RawMaterialIdEmptyException();
        }

        boolean removed = materials.removeIf(
                productMaterial -> productMaterial
                        .getRawMaterial().getId().equals(rawMaterialId)
        );

        if (!removed) {
            throw new ProductMaterialNotFoundException();
        }
    }

    public static ProductSummaryResponse getSummary() {
        Object[] result = (Object[]) getEntityManager()
                .createNativeQuery("""
                    SELECT
                        COUNT(*) as total,
                        COUNT(*) FILTER (WHERE is_active = true) as active,
                        COUNT(*) FILTER (WHERE is_active = false) as inactive
                    FROM tb_products
                """)
                .getSingleResult();

        ProductSummaryResponse summary = new ProductSummaryResponse();

        summary.setTotal(((Number) result[0]).intValue());
        summary.setActive(((Number) result[1]).intValue());
        summary.setInactive(((Number) result[2]).intValue());

        return summary;
    }
}
