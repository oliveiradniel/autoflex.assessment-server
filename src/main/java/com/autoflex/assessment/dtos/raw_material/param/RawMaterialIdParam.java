package com.autoflex.assessment.dtos.raw_material.param;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.PathParam;
import java.util.UUID;

public class RawMaterialIdParam {

    @PathParam("id")
    @NotNull(message = "Raw material ID is required.")
    public UUID id;
}
