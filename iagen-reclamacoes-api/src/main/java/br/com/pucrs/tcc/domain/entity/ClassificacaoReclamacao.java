package br.com.pucrs.tcc.domain.entity;

import br.com.pucrs.tcc.domain.ai.Taxonomia;
import jakarta.persistence.*;

public class ClassificacaoReclamacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classificacao_reclamacao_seq")
    @SequenceGenerator(
            name = "classificacao_reclamacao_seq",
            sequenceName = "classificacao_reclamacao_seq",
            allocationSize = 1
    )
    @Column(name = "id_classificao_reclamacao")
    private Long idClassificacaoReclamacao;

    private Taxonomia.Categoria categoria;

    private Taxonomia.Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reclamacao", nullable = false)
    private Reclamacao reclamacao;

    public Long getIdClassificacaoReclamacao() {
        return idClassificacaoReclamacao;
    }

    public void setIdClassificacaoReclamacao(Long idClassificacaoReclamacao) {
        this.idClassificacaoReclamacao = idClassificacaoReclamacao;
    }
}
