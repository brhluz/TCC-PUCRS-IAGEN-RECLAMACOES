package br.com.pucrs.tcc.resource.exceptionmapper;

import br.com.pucrs.tcc.domain.exception.ClassificacaoException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ClassificacaoExceptionMapper implements ExceptionMapper<ClassificacaoException> {

    @Override
    public Response toResponse(ClassificacaoException ex) {
        return Response.status(500)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ApiError("ERRO_INTERNO", ex.getMessage()))
                .build();
    }
}