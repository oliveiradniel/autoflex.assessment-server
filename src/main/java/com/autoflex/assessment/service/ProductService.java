package com.autoflex.assessment.service;

import com.autoflex.assessment.entity.ProductEntity;
import com.autoflex.assessment.exception.BusinessException;
import com.autoflex.assessment.exception.CodeAlreadyInUseException;
import com.autoflex.assessment.exception.NameAlreadyInUseException;
import com.autoflex.assessment.exception.ProductNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductService {

    public List<ProductEntity> list() {
        return ProductEntity.listAll();
    }

    public ProductEntity findById(UUID id) {
        return (ProductEntity) ProductEntity.findByIdOptional(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public ProductEntity create(ProductEntity product) {
        if (ProductEntity.existsByCode(product.code)) {
            throw new CodeAlreadyInUseException();
        }

        if (ProductEntity.existsByName(product.name)) {
            throw new NameAlreadyInUseException();
        }

        ProductEntity.persist(product);

        return product;
    }

    public ProductEntity update(UUID id, ProductEntity product) {
        ProductEntity entity = findById(id);

        if (product.code != null && product.code.length() > 20) {
            throw new BusinessException("Code must be at most 20 characters.", 422);
        }

        if (product.code != null && !product.code.equals(entity.code)) {
            if (ProductEntity.existsByCode(product.code)) {
                throw new CodeAlreadyInUseException();
            }
        }

        if (product.name != null && !product.name.equals(entity.name)) {
            if (ProductEntity.existsByName(product.name)) {
                throw new NameAlreadyInUseException();
            }
        }

        if (product.price != null
                && product.price.compareTo(new BigDecimal("0.01")) < 0) {
            throw new BusinessException("Price must be at least 0.01", 422);
        }

        if (product.description != null && product.description.length() > 500) {
            throw new BusinessException("Description must be at most 500 characters.", 422);
        }

        if (product.code != null) entity.code = product.code;
        if (product.name != null) entity.name = product.name;
        if (product.description != null) entity.description = product.description;
        if (product.price != null) entity.price = product.price;

        ProductEntity.persist(entity);

        return entity;
    }

    public void delete(UUID id) {
        findById(id);

        ProductEntity.deleteById(id);
    }
}
