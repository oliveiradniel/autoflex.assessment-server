package com.autoflex.assessment.entities;

import com.autoflex.assessment.exceptions.BusinessException;
import com.autoflex.assessment.exceptions.ProductMaterialNotFoundException;
import com.autoflex.assessment.exceptions.RawMaterialIdEmptyException;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    public UUID id;

    @Column(unique = true, length = 20, nullable = false)
    @Size(max = 20, message = "Code must be at most 20 characters.")
    @NotBlank(message = "Product code is required.")
    public String code;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Product name is required.")
    public String name;

    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    @NotNull(message = "Product price is required.")
    public BigDecimal price;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    public Boolean isActive;

    @Column(length = 500, nullable = true)
    @Size(max = 500, message = "Description must be at most 500 characters.")
    public String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private final List<ProductMaterialEntity> materials = new ArrayList<>();

    public static boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    public static boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public List<ProductMaterialEntity> getMaterials() {

        return Collections.unmodifiableList(materials);
    }

    public void addRawMaterial(
            RawMaterialEntity rawMaterial, BigDecimal quantityNeeded
    ) {

        if (rawMaterial == null || rawMaterial.id == null) {
            throw new BusinessException("Raw material is required", 422);
        }

        if (quantityNeeded == null || quantityNeeded.compareTo(MIN_QUANTITY) < 0) {
            throw new BusinessException("Quantity needed must be at least 0.01", 422);
        }

        boolean alreadyExists = materials.stream()
                .anyMatch(productMaterial -> productMaterial.rawMaterial != null
                        && productMaterial.rawMaterial.id.equals(rawMaterial.id));

        if (alreadyExists) {
            throw new BusinessException(
                    "This raw material is already part of the composition of this product.",
                    422
            );
        }

        ProductMaterialEntity productMaterial = new ProductMaterialEntity();

        productMaterial.product = this;
        productMaterial.rawMaterial = rawMaterial;
        productMaterial.quantityNeeded = quantityNeeded;

        materials.add(productMaterial);
    }

    public void upsertRawMaterial(RawMaterialEntity rawMaterial, BigDecimal quantity) {

        if (rawMaterial == null || rawMaterial.id == null) {
            throw new RawMaterialIdEmptyException();
        }

        if (quantity == null || quantity.compareTo(MIN_QUANTITY) < 0) {
            throw new BusinessException("Quantity needed must be at least 0.01", 422);
        }

        ProductMaterialEntity found = materials.stream()
                .filter(productMaterial -> productMaterial.rawMaterial != null
                        && productMaterial.rawMaterial.id.equals(rawMaterial.id))
                .findFirst()
                .orElse(null);

        if (found != null) {
            found.quantityNeeded = quantity;
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
                        .rawMaterial.id.equals(rawMaterialId)
        );

        if (!removed) {
            throw new ProductMaterialNotFoundException();
        }
    }
}
