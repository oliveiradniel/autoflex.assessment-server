package com.autoflex.assessment.entities;

import com.autoflex.assessment.enums.UnitType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_raw_materials")
public class RawMaterialEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(unique = true, length = 20, nullable = false)
    @NotBlank(message = "Raw material code is required.")
    @Size(max = 20, message = "Code must be at most 20 characters.")
    public String code;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Raw material name is required.")
    public String name;

    @Column(name = "stock_quantity", nullable = false)
    @NotNull(message = "Stock quantity is required.")
    @DecimalMin(value = "0.01", message = "Stock must be at least 0.01.")
    public BigDecimal stockQuantity;

    @Column(name = "unit_type", nullable = false)
    @NotNull(message = "Unit type is required.")
    @Enumerated(EnumType.STRING)
    public UnitType unitType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "timestamp default now()")
    public LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false,
            columnDefinition = "timestamp default now()")
    public LocalDateTime updatedAt;

    public static boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    public static boolean existsByName(String name) {
        return count("name", name) > 0;
    }
}
