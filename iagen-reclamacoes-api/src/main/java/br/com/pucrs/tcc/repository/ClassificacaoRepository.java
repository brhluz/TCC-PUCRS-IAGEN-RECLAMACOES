package br.com.pucrs.tcc.repository;

import br.com.pucrs.tcc.domain.entity.ClassificacaoReclamacao;
import br.com.pucrs.tcc.domain.entity.Reclamacao;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ClassificacaoRepository implements PanacheRepositoryBase<ClassificacaoReclamacao, Long> {

}