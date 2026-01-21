package br.com.pucrs.tcc.domain;

public class ClassificacaoItem {
    private String departamento;
    private String categoria;
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
