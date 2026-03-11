package com.forgeplan.entities;

import com.forgeplan.enums.UnitType;
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
    private UUID id;

    @Column(unique = true, length = 20, nullable = false)
    @NotBlank(message = "Raw material code is required.")
    @Size(max = 20, message = "Code must be at most 20 characters.")
    private String code;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Raw material name is required.")
    private String name;

    @Column(name = "stock_quantity", nullable = false)
    @NotNull(message = "Stock quantity is required.")
    @DecimalMin(value = "0.01", message = "Stock must be at least 0.01.")
    private BigDecimal stockQuantity;

    @Column(name = "unit_type", nullable = false)
    @NotNull(message = "Unit type is required.")
    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false,
            columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt;

    public static boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    public static boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(BigDecimal stockQuantity) { this.stockQuantity = stockQuantity; }

    public UnitType getUnitType() { return unitType; }
    public void setUnitType(UnitType unitType) { this.unitType = unitType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
