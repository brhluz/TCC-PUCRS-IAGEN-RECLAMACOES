package br.com.pucrs.tcc.resource.dto;

import br.com.pucrs.tcc.domain.entity.Reclamacao;

import java.time.LocalDateTime;

public class ReclamacaoResponse {

    private String protocolo;
    private String nome;
    private String email;
    private String descricao;
    private String status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public static ReclamacaoResponse from(Reclamacao reclamacao) {
        ReclamacaoResponse response = new ReclamacaoResponse();
        response.setProtocolo(reclamacao.getProtocolo());
        response.setNome(reclamacao.getNome());
        response.setEmail(reclamacao.getEmail());
        response.setDescricao(reclamacao.getDescricao());
        response.setStatus(reclamacao.getStatus().name());
        response.setCriadoEm(reclamacao.getCriadoEm());
        response.setAtualizadoEm(reclamacao.getAtualizadoEm());
        return response;
    }

    // Getters e Setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
}
