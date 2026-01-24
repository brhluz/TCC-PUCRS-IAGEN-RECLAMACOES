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

            if (response == null || response.getClassificacoes() == null || response.getClassificacoes().isEmpty()) {
                LOG.warn("IA não conseguiu classificar. Encaminhando para triagem humana.");
                return criarRespostaVazia();
            }

            boolean invalido = response.getClassificacoes().stream()
                    .anyMatch(item ->
                            (item.getDepartamento()==null || item.getCategoria()==null || item.getMotivoExtraido()==null)
            );

            if(invalido){
                LOG.warn("IA gerou respostas incosistentes. Encaminhando para triagem humana.");
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
            LOG.error("Falha na classificação automática. Marcando para triagem humana.", e);
            throw new ClassificacaoException("Falha interna ao classificar reclamação.", e);
        }
    }

    private ClassificacaoResponse criarRespostaVazia() {
        ClassificacaoResponse response = new ClassificacaoResponse();
        response.setClassificacoes(Collections.emptyList());
        return response;
    }
}
