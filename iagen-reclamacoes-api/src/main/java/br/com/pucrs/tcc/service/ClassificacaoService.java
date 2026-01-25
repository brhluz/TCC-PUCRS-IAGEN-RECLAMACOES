package br.com.pucrs.tcc.service;

import br.com.pucrs.tcc.domain.ClassificacaoItem;
import br.com.pucrs.tcc.domain.ClassificacaoResponse;
import br.com.pucrs.tcc.domain.ai.ReclamacaoAiService;
import br.com.pucrs.tcc.domain.entity.ClassificacaoReclamacao;
import br.com.pucrs.tcc.domain.entity.Reclamacao;
import br.com.pucrs.tcc.domain.exception.ClassificacaoException;
import br.com.pucrs.tcc.repository.ClassificacaoRepository;
import br.com.pucrs.tcc.repository.ReclamacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ClassificacaoService {

    private static final Logger LOG = Logger.getLogger(ClassificacaoService.class);

    @Inject
    ReclamacaoAiService aiService;

    @Inject
    ClassificacaoRepository classificacaoRepository;

    @Inject
    ReclamacaoRepository reclamacaoRepository;

    public ClassificacaoResponse classificar(String descricao) {
        return this.classificar(descricao, null);
    }

    public ClassificacaoResponse classificar(String descricao, Long idReclamacao) {
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

            if(idReclamacao==null){
                return response;
            }

            Reclamacao reclamacao = reclamacaoRepository.findById(idReclamacao);

            for(ClassificacaoItem classificacaoItem : response.getClassificacoes()){

                ClassificacaoReclamacao classificacaoReclamacao = new ClassificacaoReclamacao();
                classificacaoReclamacao.setReclamacao(reclamacao);
                classificacaoReclamacao.setDepartamento(classificacaoItem.getDepartamento());
                classificacaoReclamacao.setCategoria(classificacaoItem.getCategoria());
                classificacaoReclamacao.setMotivoExtraido(classificacaoItem.getMotivoExtraido());

                classificacaoRepository.persist(classificacaoReclamacao);
            }

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
