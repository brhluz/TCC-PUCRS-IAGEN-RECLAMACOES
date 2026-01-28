package br.com.pucrs.tcc.domain.dto;

import java.util.List;

public class ClassificacoesDTO {
    private String departamento;
    private String categorias;

    public ClassificacoesDTO() {}

    public ClassificacoesDTO(String departamento, String categorias) {
        this.departamento = departamento;
        this.categorias = categorias;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }
}
