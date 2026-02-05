package com.autoflex.assessment.controller;

import com.autoflex.assessment.entity.RawMaterialEntity;
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

    @GET
    public Response list() {
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok().build();
    }

    @Context
    UriInfo uriInfo;
    @POST
    @Transactional
    public Response create(@Valid RawMaterialEntity product) {
        // var uri = uriInfo.getAbsolutePathBuilder().path(createdRawMaterial.id.toString()).build();

        return Response.ok().build();
    }

    @PATCH
    @Transactional
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, RawMaterialEntity product) {
        return Response.ok().build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        return Response.noContent().build();
    }
}
