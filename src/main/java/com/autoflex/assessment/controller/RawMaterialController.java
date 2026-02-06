package com.autoflex.assessment.controller;

import com.autoflex.assessment.entity.RawMaterialEntity;
import com.autoflex.assessment.service.RawMaterialService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.UUID;

@Path("/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @GET
    public Response list() {
        return Response.ok(rawMaterialService.list()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok(rawMaterialService.findById(id)).build();
    }

    @Context
    UriInfo uriInfo;
    @POST
    @Transactional
    public Response create(@Valid RawMaterialEntity rawMaterial) {
        var createdRawMaterial = rawMaterialService.create(rawMaterial);

        var uri = uriInfo.getAbsolutePathBuilder().path(createdRawMaterial.id.toString()).build();

        return Response.created(uri).entity(createdRawMaterial).build();
    }

    @PATCH
    @Transactional
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, RawMaterialEntity rawMaterial) {
        return Response.ok(rawMaterialService.update(id, rawMaterial)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        rawMaterialService.delete(id);

        return Response.noContent().build();
    }
}
