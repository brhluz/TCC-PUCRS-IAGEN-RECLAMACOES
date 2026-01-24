package br.com.pucrs.tcc.domain;

import br.com.pucrs.tcc.domain.ai.Taxonomia;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClassificacaoResponseTest {

    @Test
    void deveRetornarListaVaziaQuandoIaNaoTemSeguranca() {
        ClassificacaoResponse response = new ClassificacaoResponse(List.of());

        assertNotNull(response.getClassificacoes());
        assertTrue(response.getClassificacoes().isEmpty());
    }

    @Test
    void deveRetornarUmaClassificacao() {
        ClassificacaoItem item = new ClassificacaoItem(
            Taxonomia.Departamento.LOGISTICA,
            Taxonomia.Categoria.ATRASO_ENTREGA,
            ""
        );
        ClassificacaoResponse response = new ClassificacaoResponse(List.of(item));

        assertEquals(1, response.getClassificacoes().size());
        assertEquals(Taxonomia.Departamento.LOGISTICA, response.getClassificacoes().get(0).getDepartamento());
        assertEquals(Taxonomia.Categoria.ATRASO_ENTREGA, response.getClassificacoes().get(0).getCategoria());
    }

    @Test
    void deveRetornarMultiplasClassificacoes() {
        ClassificacaoItem item1 = new ClassificacaoItem(
            Taxonomia.Departamento.LOGISTICA,
            Taxonomia.Categoria.PRODUTO_NAO_ENTREGUE,
            ""
        );
        ClassificacaoItem item2 = new ClassificacaoItem(
            Taxonomia.Departamento.ATENDIMENTO_CLIENTE,
            Taxonomia.Categoria.DEMORA_ATENDIMENTO,
            ""
        );

        ClassificacaoResponse response = new ClassificacaoResponse(List.of(item1, item2));

        assertEquals(2, response.getClassificacoes().size());
    }
}
