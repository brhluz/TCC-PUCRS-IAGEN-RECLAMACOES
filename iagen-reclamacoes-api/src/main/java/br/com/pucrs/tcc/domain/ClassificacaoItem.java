package br.com.pucrs.tcc.domain;

public class ClassificacaoItem {
    private String departamento;
    private String categoria;

    public ClassificacaoItem() {
    }

    public ClassificacaoItem(String departamento, String categoria) {
        this.departamento = departamento;
        this.categoria = categoria;
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
}
