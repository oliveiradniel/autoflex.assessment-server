package com.forgeplan.filters;

import com.forgeplan.exceptions.EmptyJsonException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(Priorities.USER)
public class EmptyJsonFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();

        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PATCH") || method.equalsIgnoreCase("PUT")) {

            if (!requestContext.hasEntity() || requestContext.getLength() == 0) {
                throw new EmptyJsonException();
            }
        }
    }
}
