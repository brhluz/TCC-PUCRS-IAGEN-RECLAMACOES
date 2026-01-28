package br.com.pucrs.tcc.resource.exceptionmapper;

import br.com.pucrs.tcc.domain.exception.ClassificacaoException;
import br.com.pucrs.tcc.resource.exceptionmapper.ApiError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException ex) {
        int status = ex.getResponse() != null ? ex.getResponse().getStatus() : 500;

        if (status == 429) {
            return Response.status(429)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Retry-After", "20")
                    .entity(new ApiError("QUOTA_EXCEDIDA", "Limite do provedor de IA atingido. Tente novamente."))
                    .build();
        }

        if (status == 401 || status == 403) {
            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new ApiError("IA_NAO_AUTORIZADA", "Falha de autenticação/autorização no provedor de IA."))
                    .build();
        }

        if (status >= 500) {
            return Response.status(503)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new ApiError("IA_INDISPONIVEL", "Provedor de IA indisponível no momento."))
                    .build();
        }

        // outros 4xx
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ApiError("ERRO_AO_CHAMAR_SERVICO", ex.getMessage()))
                .build();
    }
}