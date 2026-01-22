package br.com.pucrs.tcc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClassificacaoItem {
    @NotBlank
    private String departamento;

    @NotBlank
    private String categoria;

    @NotBlank
    @Size(max = 200)
    private String motivoExtraido;

    public ClassificacaoItem() {
    }

    public ClassificacaoItem(String departamento, String categoria, String motivoExtraido) {
        this.departamento = departamento;
        this.categoria = categoria;
        this.motivoExtraido = motivoExtraido;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMotivoExtraido() {
        return motivoExtraido;
    }

    public void setMotivoExtraido(String motivoExtraido) {
        this.motivoExtraido = motivoExtraido;
    }
}
