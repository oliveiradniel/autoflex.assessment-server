package com.autoflex.assessment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    public UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    public ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "raw_material_id", nullable = false)
    @JsonIgnore
    public RawMaterialEntity rawMaterial;

    @Column(name = "quantity_needed")
    @DecimalMin(value = "0.01", message = "Quantity needed must be at least 0.01")
    public BigDecimal quantityNeeded;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
}
