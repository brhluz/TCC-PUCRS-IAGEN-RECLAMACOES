package br.com.pucrs.tcc.domain.dto;

import br.com.pucrs.tcc.domain.entity.Reclamacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReclamacaoClassificadaDTO {
    private String protocolo;
    private String descricao;
    private boolean classificada;
    private Map<String, List<String>> categoriasPorDepartamento;
    private LocalDateTime ultimaAtualizacao;
    private String statusAtendimento;

    public Map<String, List<String>> getCategoriasPorDepartamento() {
        return categoriasPorDepartamento;
    }

    public void setCategoriasPorDepartamento(Map<String, List<String>> categoriasPorDepartamento) {
        this.categoriasPorDepartamento = categoriasPorDepartamento;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isClassificada() {
        return classificada;
    }

    public void setClassificada(boolean classificada) {
        this.classificada = classificada;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getStatusAtendimento() {
        return statusAtendimento;
    }

    public void setStatusAtendimento(String statusAtendimento) {
        this.statusAtendimento = statusAtendimento;
    }

    public static ReclamacaoClassificadaDTO from(Reclamacao reclamacao) {
        ReclamacaoClassificadaDTO dto = new ReclamacaoClassificadaDTO();
        dto.setProtocolo(reclamacao.getProtocolo());
        dto.setDescricao(reclamacao.getDescricao());
        dto.setClassificada(false);
        dto.setUltimaAtualizacao(reclamacao.getAtualizadoEm());
        dto.setStatusAtendimento(reclamacao.getStatus().name());

        if (reclamacao.getClassificacoes() != null) {
            Map<String, List<String>> mapa = reclamacao.getClassificacoes().stream()
                    .collect(Collectors.groupingBy(
                            c -> c.getDepartamento().getDescricao(),
                            Collectors.mapping(c -> c.getCategoria().getDescricao(), Collectors.toList())
                    ));
            dto.setCategoriasPorDepartamento(mapa);
        }
        return dto;
    }
}
