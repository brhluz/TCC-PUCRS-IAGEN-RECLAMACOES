package br.com.pucrs.tcc.repository;

import br.com.pucrs.tcc.domain.entity.ClassificacaoReclamacao;
import br.com.pucrs.tcc.domain.entity.HistoricoReclamacao;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HistoricoReclamacaoRepository implements PanacheRepositoryBase<HistoricoReclamacao, Long> {

}