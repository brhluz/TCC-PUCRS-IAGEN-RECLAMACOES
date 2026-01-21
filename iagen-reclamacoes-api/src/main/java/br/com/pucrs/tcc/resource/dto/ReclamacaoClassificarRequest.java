package br.com.pucrs.tcc.resource.dto;

import jakarta.validation.constraints.NotBlank;

public class ReclamacaoClassificarRequest {

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    public ReclamacaoClassificarRequest() {
    }

    public ReclamacaoClassificarRequest(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
