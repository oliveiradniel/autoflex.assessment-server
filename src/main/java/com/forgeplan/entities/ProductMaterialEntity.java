package com.forgeplan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_product_materials",
    uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "raw_material_id"}))
public class ProductMaterialEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterialEntity rawMaterial;

    @Column(name = "quantity_needed", nullable = false)
    @DecimalMin(value = "0.01", message = "Quantity needed must be at least 0.01")
    private BigDecimal quantityNeeded;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false,
            columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public ProductEntity getProduct() { return product; }
    public void setProduct(ProductEntity product) { this.product = product; }

    public RawMaterialEntity getRawMaterial() { return rawMaterial; }
    public void setRawMaterial(RawMaterialEntity rawMaterial) { this.rawMaterial = rawMaterial; }

    public BigDecimal getQuantityNeeded() { return quantityNeeded; }
    public void setQuantityNeeded(BigDecimal quantityNeeded) { this.quantityNeeded = quantityNeeded; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
