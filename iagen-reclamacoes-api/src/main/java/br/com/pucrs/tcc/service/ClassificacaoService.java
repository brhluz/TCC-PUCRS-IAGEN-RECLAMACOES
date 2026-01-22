package br.com.pucrs.tcc.service;

import br.com.pucrs.tcc.domain.ClassificacaoResponse;
import br.com.pucrs.tcc.domain.ai.ReclamacaoAiService;
import br.com.pucrs.tcc.domain.exception.ClassificacaoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;

import java.util.Collections;

@ApplicationScoped
public class ClassificacaoService {

    private static final Logger LOG = Logger.getLogger(ClassificacaoService.class);

    @Inject
    ReclamacaoAiService aiService;

    public ClassificacaoResponse classificar(String descricao) {
        LOG.info("Iniciando classificação automática via IA");

        try {
            ClassificacaoResponse response = aiService.classificar(descricao);

            if (response == null || response.getClassificacoes() == null) {
                LOG.warn("IA retornou resposta nula ou sem classificações");
                return criarRespostaVazia();
            }

            LOG.infof("Classificação concluída: %d item(ns)", response.getClassificacoes().size());
            return response;

        } catch (WebApplicationException e) {
            LOG.errorf(e, "Falha ao chamar provedor de IA (status=%s): %s",
                    (e.getResponse() != null ? e.getResponse().getStatus() : "null"),
                    e.getMessage());
            throw e;

        } catch (Exception e) {
            LOG.error("Erro interno ao classificar reclamação", e);
            throw new ClassificacaoException("Falha interna ao classificar reclamação.", e);
        }
    }

    private ClassificacaoResponse criarRespostaVazia() {
        ClassificacaoResponse response = new ClassificacaoResponse();
        response.setClassificacoes(Collections.emptyList());
        return response;
    }
}
