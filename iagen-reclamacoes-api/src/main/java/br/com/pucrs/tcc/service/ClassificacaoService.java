package br.com.pucrs.tcc.service;

import br.com.pucrs.tcc.domain.ClassificacaoResponse;
import br.com.pucrs.tcc.domain.ai.ReclamacaoAiService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Collections;

@ApplicationScoped
public class ClassificacaoService {

    private static final Logger LOG = Logger.getLogger(ClassificacaoService.class);

    @Inject
    ReclamacaoAiService aiService;

    public ClassificacaoResponse classificar(String descricao) {
        try {
            LOG.infof("Iniciando classificação automática via IA");
            ClassificacaoResponse response = aiService.classificar(descricao);

            if (response == null || response.getClassificacoes() == null) {
                LOG.warn("IA retornou resposta nula ou sem classificações");
                return criarRespostaVazia();
            }

            LOG.infof("Classificação concluída: %d departamento(s) identificado(s)",
                      response.getClassificacoes().size());
            return response;

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao classificar reclamação via IA: %s", e.getMessage());
            return criarRespostaVazia();
        }
    }

    private ClassificacaoResponse criarRespostaVazia() {
        ClassificacaoResponse response = new ClassificacaoResponse();
        response.setClassificacoes(Collections.emptyList());
        return response;
    }
}
