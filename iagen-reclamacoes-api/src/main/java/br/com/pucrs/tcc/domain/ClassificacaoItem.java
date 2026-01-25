package br.com.pucrs.tcc.domain;

import br.com.pucrs.tcc.domain.ai.Taxonomia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClassificacaoItem {
    private Taxonomia.Departamento departamento;

    private Taxonomia.Categoria categoria;

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
