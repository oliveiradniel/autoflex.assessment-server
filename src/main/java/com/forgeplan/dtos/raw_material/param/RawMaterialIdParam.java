package com.forgeplan.dtos.raw_material.param;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.PathParam;
import java.util.UUID;

public class RawMaterialIdParam {

    @PathParam("id")
    @NotNull(message = "Raw material ID is required.")
    private UUID id;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
