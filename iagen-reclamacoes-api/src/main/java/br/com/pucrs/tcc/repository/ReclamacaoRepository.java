package br.com.pucrs.tcc.repository;

import br.com.pucrs.tcc.domain.entity.Reclamacao;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ReclamacaoRepository implements PanacheRepositoryBase<Reclamacao, Long> {

    public Optional<Reclamacao> findByProtocolo(String protocolo) {
        return find("from Reclamacao r left join fetch r.classificacoes where r.protocolo = ?1", protocolo)
                .firstResultOptional();
    }
}