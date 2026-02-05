package com.autoflex.assessment.controller;

import com.autoflex.assessment.entity.ProductEntity;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.UUID;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController  {

    @GET
    public Response list() {
        return Response.ok("All products").build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok("Product found " + id).build();
    }

    @Context
    UriInfo uriInfo;
    @POST
    @Transactional
    public Response create(ProductEntity product) {
        // var uri = uriInfo.getAbsolutePathBuilder().path(product.id.toString()).build();

        return Response.status(201).entity("Created product").build();
    }

    @PATCH
    @Transactional
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, ProductEntity product) {
        return Response.noContent().build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        return Response.noContent().build();
    }
}
