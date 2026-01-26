package br.com.pucrs.tcc.service;

import br.com.pucrs.tcc.domain.ai.ClassificacaoResponse;
import br.com.pucrs.tcc.domain.dto.ReclamacaoClassificadaDTO;
import br.com.pucrs.tcc.domain.entity.Reclamacao;
import br.com.pucrs.tcc.repository.ReclamacaoRepository;
import br.com.pucrs.tcc.resource.dto.ReclamacaoRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReclamacaoService {

    private static final Logger LOG = Logger.getLogger(ReclamacaoService.class);

    @Inject
    MailService mailService;

    @Inject
    ReclamacaoRepository reclamacaoRepository;

    @Inject
    ClassificacaoService classificacaoService;

    @Transactional
    public Reclamacao criar(ReclamacaoRequest request) {
        Reclamacao reclamacao;

        LOG.infof("Criando reclamação para %s (%s)", request.getNome(), request.getEmail());

        // Criar entidade
        reclamacao = new Reclamacao();
        reclamacao.setNome(request.getNome());
        reclamacao.setEmail(request.getEmail());
        reclamacao.setDescricao(request.getDescricao());

        // Persistir
        reclamacaoRepository.persist(reclamacao);


        LOG.infof("Reclamação criada com protocolo: %s", reclamacao.getProtocolo());

        // Enviar notificação
        mailService.enviarNotificacaoProtocolo(
            reclamacao.getEmail(),
            reclamacao.getProtocolo(),
            reclamacao.getNome()
        );

        ClassificacaoResponse classificacaoResponse = classificacaoService.classificar(reclamacao.getDescricao(), reclamacao.getIdReclamacao());

        if(classificacaoResponse.getClassificacoes() == null || classificacaoResponse.getClassificacoes().isEmpty()){
            reclamacao.setStatus(Reclamacao.StatusReclamacao.PENDENTE_TRIAGEM_HUMANA);
        }

        return reclamacao;
    }

    public ReclamacaoClassificadaDTO retornarReclamacaoClassificadaPorProtocolo(String protocolo) {
        ReclamacaoClassificadaDTO reclamacaoClassificadaDTO;
        Reclamacao reclamacao = reclamacaoRepository.findByProtocolo(protocolo)
                .orElseThrow(() -> new WebApplicationException("Reclamação não encontrada", 404));

        reclamacaoClassificadaDTO = ReclamacaoClassificadaDTO.from(reclamacao);

        if(!Reclamacao.StatusReclamacao.PENDENTE_TRIAGEM_HUMANA.equals(reclamacao.getStatus())){
            reclamacaoClassificadaDTO.setClassificada(true);
        }

        return reclamacaoClassificadaDTO;
    }

}
