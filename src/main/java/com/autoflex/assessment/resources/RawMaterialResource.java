package com.autoflex.assessment.resources;

import com.autoflex.assessment.dtos.product.param.ProductIdParam;
import com.autoflex.assessment.dtos.raw_material.request.RawMaterialCreateRequest;
import com.autoflex.assessment.dtos.raw_material.request.RawMaterialUpdateRequest;
import com.autoflex.assessment.services.RawMaterialService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialResource {

    private final RawMaterialService rawMaterialService;

    public RawMaterialResource(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @GET
    public Response list() {
        return Response.ok(rawMaterialService.list()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@BeanParam @Valid ProductIdParam param) {
        return Response.ok(rawMaterialService.findById(param.id)).build();
    }

    @Context
    UriInfo uriInfo;
    @POST
    @Transactional
    public Response create(@Valid RawMaterialCreateRequest rawMaterial) {
        var createdRawMaterial = rawMaterialService.create(rawMaterial);

        var uri = uriInfo.getAbsolutePathBuilder().path(createdRawMaterial.id.toString()).build();

        return Response.created(uri).entity(createdRawMaterial).build();
    }

    @PATCH
    @Transactional
    @Path("/{id}")
    public Response update(@BeanParam @Valid ProductIdParam param, RawMaterialUpdateRequest rawMaterial) {
        return Response.ok(rawMaterialService.update(param.id, rawMaterial)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@BeanParam @Valid ProductIdParam param) {
        rawMaterialService.delete(param.id);

        return Response.noContent().build();
    }
}
