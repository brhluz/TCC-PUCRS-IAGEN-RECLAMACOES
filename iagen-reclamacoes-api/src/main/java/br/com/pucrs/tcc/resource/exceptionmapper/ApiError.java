package br.com.pucrs.tcc.resource.exceptionmapper;

public class ApiError {
    public String codigo;
    public String mensagem;

    public ApiError() {}

    public ApiError(String codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }
}