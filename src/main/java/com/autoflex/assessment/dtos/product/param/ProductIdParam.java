package com.autoflex.assessment.dtos.product.param;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.PathParam;

import java.util.UUID;

public class ProductIdParam {

    @PathParam("id")
    @NotNull(message = "Product ID is required.")
    public UUID id;
}
