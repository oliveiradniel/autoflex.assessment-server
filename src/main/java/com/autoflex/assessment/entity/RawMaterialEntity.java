package com.autoflex.assessment.entity;

import com.autoflex.assessment.enums.UnitType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_raw_materials")
public class RawMaterialEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(unique = true, length = 20)
    @NotBlank(message = "Raw material code is required.")
    public String code;

    @Column(unique = true)
    @NotBlank(message = "Raw material name is required.")
    public String name;

    @Column(name = "stock_quantity")
    @NotNull(message = "Stock quantity is required.")
    @Min(value = 0, message = "Stock quantity cannot be negative.")
    public Integer stockQuantity;

    @Column(name = "unit_type")
    @NotNull(message = "Unit type is required.")
    @Enumerated(EnumType.STRING)
    public UnitType unitType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
}
