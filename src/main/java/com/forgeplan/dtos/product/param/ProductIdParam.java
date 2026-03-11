package com.forgeplan.dtos.product.param;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.PathParam;

import java.util.UUID;

public class ProductIdParam {

    @PathParam("id")
    @NotNull(message = "Product ID is required.")
    private UUID id;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
