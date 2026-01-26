package br.com.pucrs.tcc.domain.ai;

import java.util.List;

public class ClassificacaoResponse {
    private List<ClassificacaoItem> classificacoes;

    public ClassificacaoResponse() {
    }

    public ClassificacaoResponse(List<ClassificacaoItem> classificacoes) {
        this.classificacoes = classificacoes;
    }

    public List<ClassificacaoItem> getClassificacoes() {
        return classificacoes;
    }

    public void setClassificacoes(List<ClassificacaoItem> classificacoes) {
        this.classificacoes = classificacoes;
    }
}
