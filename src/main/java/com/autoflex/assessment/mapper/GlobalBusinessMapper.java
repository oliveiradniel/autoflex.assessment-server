package com.autoflex.assessment.mapper;

import com.autoflex.assessment.exception.BusinessException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class GlobalBusinessMapper implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException exception) {
        return Response.status(exception.getStatusCode()) // Unprocessable Entity (comum para erros de negócio)
                .entity(Map.of(
                        "type", exception.getClass().getSimpleName(),
                        "status", exception.getStatusCode(),
                        "error", exception.getMessage()
                ))
                .build();
    }
}
