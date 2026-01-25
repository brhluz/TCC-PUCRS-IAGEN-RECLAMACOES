package br.com.pucrs.tcc.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reclamacao")
public class Reclamacao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reclamacao_seq")
    @SequenceGenerator(
            name = "reclamacao_seq",
            sequenceName = "reclamacao_seq",
            allocationSize = 1
    )
    @Column(name = "id_reclamacao")
    private Long idReclamacao;

    @Column(length = 36)
    private String protocolo;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, length = 200)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private StatusReclamacao status;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        if (this.protocolo == null) {
            this.protocolo = UUID.randomUUID().toString();
        }
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusReclamacao.RECEBIDA;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public Long getIdReclamacao() {
        return idReclamacao;
    }

    public void setIdReclamacao(Long idReclamacao) {
        this.idReclamacao = idReclamacao;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusReclamacao getStatus() {
        return status;
    }

    public void setStatus(StatusReclamacao status) {
        this.status = status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public enum StatusReclamacao {
        RECEBIDA("Recebida"),
        EM_ATENDIMENTO("Em Atendimento"),
        FINALIZADO("Finalizado"),
        PENDENTE_TRIAGEM_HUMANA("Pendente Triagem Humana");

        private final String descricao;

        StatusReclamacao(String descricao) {
            this.descricao = descricao;
        }
    }
}