package com.forgeplan.resources;

import com.forgeplan.dtos.product.param.ProductIdParam;
import com.forgeplan.dtos.product.request.ProductCreateRequest;
import com.forgeplan.dtos.product.request.ProductUpdateRequest;
import com.forgeplan.dtos.product.response.ProductResponse;
import com.forgeplan.services.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.BeanParam;

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
    public Response findById(@BeanParam @Valid ProductIdParam param) {
        return Response.ok(productService.findById(param.getId())).build();
    }

    @GET
    @Path("/summary")
    public Response summary() {
        return Response.ok(productService.getSummary()).build();
    }

    @Context
    UriInfo uriInfo;
    @POST
    @Transactional
    public Response create(@Valid ProductCreateRequest product) {
        ProductResponse createdProduct = productService.create(product);

        var uri = uriInfo.getAbsolutePathBuilder().path(createdProduct.getId().toString()).build();

        return Response.created(uri).entity(createdProduct).build();
    }

    @GET
    @Path("/calculate-production")
    public Response calculateProduction() {
        return Response.ok(productService.calculateProduction()).build();
    }

    @PATCH
    @Transactional
    @Path("/{id}")
    public Response update(
            @BeanParam @Valid ProductIdParam param,
            ProductUpdateRequest product
    ) {
        return Response.ok(productService.update(param.getId(), product)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@BeanParam @Valid ProductIdParam param) {
        productService.delete(param.getId());

        return Response.noContent().build();
    }
}
