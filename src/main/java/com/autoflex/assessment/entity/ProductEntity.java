package com.autoflex.assessment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_products")
public class ProductEntity extends PanacheEntityBase {

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

    @Column(length = 500, nullable = true)
    @Size(max = 500, message = "Description must be at most 500 characters.")
    public String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ProductMaterialEntity> materials;

    public static boolean existsById(UUID id) {
        return findById(id) != null;
    }

    public static boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    public static boolean existsByName(String name) {
        return count("name", name) > 0;
    }
}
