package br.com.pucrs.tcc.resource;

import br.com.pucrs.tcc.domain.ClassificacaoResponse;
import br.com.pucrs.tcc.domain.entity.Reclamacao;
import br.com.pucrs.tcc.resource.dto.ReclamacaoClassificarRequest;
import br.com.pucrs.tcc.resource.dto.ReclamacaoRequest;
import br.com.pucrs.tcc.resource.dto.ReclamacaoResponse;
import br.com.pucrs.tcc.service.ClassificacaoService;
import br.com.pucrs.tcc.service.MailService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/reclamacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReclamacaoResource {

    private static final Logger LOG = Logger.getLogger(ReclamacaoResource.class);

    @Inject
    MailService mailService;

    @Inject
    ClassificacaoService classificacaoService;

    @POST
    @Transactional
    public Response criar(@Valid ReclamacaoRequest request) {
        LOG.infof("Recebendo nova reclamação de %s (%s)", request.getNome(), request.getEmail());

        // Criar entidade
        Reclamacao reclamacao = new Reclamacao();
        reclamacao.setNome(request.getNome());
        reclamacao.setEmail(request.getEmail());
        reclamacao.setDescricao(request.getDescricao());

        // Persistir
        reclamacao.persist();

        LOG.infof("Reclamação criada com protocolo: %s", reclamacao.getProtocolo());

        // Enviar email
        mailService.enviarNotificacaoProtocolo(
            reclamacao.getEmail(),
            reclamacao.getProtocolo(),
            reclamacao.getNome()
        );

        // Retornar resposta
        return Response
            .status(Response.Status.CREATED)
            .entity(ReclamacaoResponse.from(reclamacao))
            .build();
    }

    @POST
    @Path("/classificar")
    public Response classificar(@Valid ReclamacaoClassificarRequest request) {
        LOG.infof("Recebendo solicitação de classificação");

        ClassificacaoResponse classificacao = classificacaoService.classificar(request.getDescricao());

        return Response
            .ok(classificacao)
            .build();
    }
}
