package br.com.pucrs.tcc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClassificacaoItem {
    @NotBlank
    private Taxonomia.Departamento departamento;

    @NotNull
    private Taxonomia.Categoria categoria;

    @NotNull
    @Size(max = 200)
    private String motivoExtraido;

    public ClassificacaoItem() {
    }

    public ClassificacaoItem(Taxonomia.Departamento departamento, Taxonomia.Categoria categoria, String motivoExtraido) {
        this.departamento = departamento;
        this.categoria = categoria;
        this.motivoExtraido = motivoExtraido;
    }

    public Taxonomia.Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Taxonomia.Departamento departamento) {
        this.departamento = departamento;
    }

    public Taxonomia.Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Taxonomia.Categoria categoria) {
        this.categoria = categoria;
    }

    public String getMotivoExtraido() {
        return motivoExtraido;
    }

    public void setMotivoExtraido(String motivoExtraido) {
        this.motivoExtraido = motivoExtraido;
    }
}
