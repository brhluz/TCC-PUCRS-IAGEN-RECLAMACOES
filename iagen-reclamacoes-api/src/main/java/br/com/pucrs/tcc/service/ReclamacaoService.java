package br.com.pucrs.tcc.service;

import br.com.pucrs.tcc.domain.entity.Reclamacao;
import br.com.pucrs.tcc.repository.ReclamacaoRepository;
import br.com.pucrs.tcc.resource.dto.ReclamacaoRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReclamacaoService {

    private static final Logger LOG = Logger.getLogger(ReclamacaoService.class);

    @Inject
    MailService mailService;

    @Inject
    ReclamacaoRepository reclamacaoRepository;

    @Transactional
    public Reclamacao criar(ReclamacaoRequest request) {
        LOG.infof("Criando reclamação para %s (%s)", request.getNome(), request.getEmail());

        // Criar entidade
        Reclamacao reclamacao = new Reclamacao();
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

        return reclamacao;
    }
}
