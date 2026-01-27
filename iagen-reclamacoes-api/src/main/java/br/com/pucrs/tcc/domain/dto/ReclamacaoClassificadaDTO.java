package br.com.pucrs.tcc.domain.dto;

import br.com.pucrs.tcc.domain.entity.ClassificacaoReclamacao;
import br.com.pucrs.tcc.domain.entity.Reclamacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReclamacaoClassificadaDTO {
    private String protocolo;
    private String descricao;
    private boolean classificada;
    private List<ClassificacoesDTO> categoriasPorDepartamento;
    private LocalDateTime ultimaAtualizacao;
    private String statusAtendimento;

    public List<ClassificacoesDTO> getCategoriasPorDepartamento() {
        return categoriasPorDepartamento;
    }

    public void setCategoriasPorDepartamento(List<ClassificacoesDTO> categoriasPorDepartamento) {
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

        if (reclamacao.getClassificacoes() != null && !reclamacao.getClassificacoes().isEmpty()) {
            dto.setCategoriasPorDepartamento(reclamacao.getClassificacoes().stream()
                    .filter(c -> c.getDepartamento() != null && c.getCategoria() != null)
                    .map(c -> new ClassificacoesDTO(c.getDepartamento().getDescricao(), c.getCategoria().getDescricao()))
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
