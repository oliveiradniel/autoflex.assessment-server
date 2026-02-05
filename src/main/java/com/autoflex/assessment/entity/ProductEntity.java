package com.autoflex.assessment.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_products")
public class ProductEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(unique = true, length = 20)
    @NotBlank(message = "Product code is required.")
    public String code;

    @Column(unique = true)
    @NotBlank(message = "Product name is required.")
    public String name;

    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    @NotNull(message = "Product price is required.")
    public BigDecimal price;

    @Column(length = 500, nullable = true)
    public String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
}
