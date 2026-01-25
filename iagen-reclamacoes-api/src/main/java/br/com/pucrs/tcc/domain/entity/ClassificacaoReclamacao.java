package br.com.pucrs.tcc.domain.entity;

import br.com.pucrs.tcc.domain.ai.Taxonomia;
import jakarta.persistence.*;

@Entity
@Table(name = "classificacao_reclamacao")
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

    @Column(name = "categoria", length = 90, nullable = false)
    @Enumerated(EnumType.STRING)
    private Taxonomia.Categoria categoria;

    @Column(name = "departamento", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Taxonomia.Departamento departamento;

    @Column(name = "motivo_extraido", length = 200, nullable = false)
    private String motivoExtraido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reclamacao", nullable = false)
    private Reclamacao reclamacao;

    public Long getIdClassificacaoReclamacao() {
        return idClassificacaoReclamacao;
    }

    public void setIdClassificacaoReclamacao(Long idClassificacaoReclamacao) {
        this.idClassificacaoReclamacao = idClassificacaoReclamacao;
    }

    public Taxonomia.Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Taxonomia.Categoria categoria) {
        this.categoria = categoria;
    }

    public Taxonomia.Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Taxonomia.Departamento departamento) {
        this.departamento = departamento;
    }

    public Reclamacao getReclamacao() {
        return reclamacao;
    }

    public void setReclamacao(Reclamacao reclamacao) {
        this.reclamacao = reclamacao;
    }

    public String getMotivoExtraido() {
        return motivoExtraido;
    }

    public void setMotivoExtraido(String motivoExtraido) {
        this.motivoExtraido = motivoExtraido;
    }
}
