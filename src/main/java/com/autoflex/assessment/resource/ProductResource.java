package com.autoflex.assessment.resource;

import com.autoflex.assessment.entity.ProductEntity;
import com.autoflex.assessment.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.UUID;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    public Response list() {
        return Response.ok(productService.list()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok(productService.findById(id)).build();
    }

    @Context
    UriInfo uriInfo;
    @POST
    @Transactional
    public Response create(@Valid ProductEntity product) {
        var createdProduct = productService.create(product);

        var uri = uriInfo.getAbsolutePathBuilder().path(createdProduct.id.toString()).build();

        return Response.created(uri).entity(createdProduct).build();
    }

    @PATCH
    @Transactional
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, ProductEntity product) {
        return Response.ok(productService.update(id, product)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        productService.delete(id);

        return Response.noContent().build();
    }
}
