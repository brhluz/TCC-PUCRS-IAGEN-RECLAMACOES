package br.com.pucrs.tcc.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_reclamacao")
public class HistoricoReclamacao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historico_reclamacao_seq")
    @SequenceGenerator(
            name = "historico_reclamacao_seq",
            sequenceName = "historico_reclamacao_seq",
            allocationSize = 1
    )
    @Column(name = "id_historico")
    private Long idHistorico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reclamacao", nullable = false)
    private Reclamacao reclamacao;

    @Column(name = "status_anterior", length = 50)
    @Enumerated(EnumType.STRING)
    private Reclamacao.StatusReclamacao statusAnterior;

    @Column(name = "status_novo", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Reclamacao.StatusReclamacao statusNovo;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "data_alteracao", nullable = false, updatable = false)
    private LocalDateTime dataAlteracao;

    @PrePersist
    public void prePersist() {
        this.dataAlteracao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(Long idHistorico) {
        this.idHistorico = idHistorico;
    }

    public Reclamacao getReclamacao() {
        return reclamacao;
    }

    public void setReclamacao(Reclamacao reclamacao) {
        this.reclamacao = reclamacao;
    }

    public Reclamacao.StatusReclamacao getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(Reclamacao.StatusReclamacao statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public Reclamacao.StatusReclamacao getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(Reclamacao.StatusReclamacao statusNovo) {
        this.statusNovo = statusNovo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}
