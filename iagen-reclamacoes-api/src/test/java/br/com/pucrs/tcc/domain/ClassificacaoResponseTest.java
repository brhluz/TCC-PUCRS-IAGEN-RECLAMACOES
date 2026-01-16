package br.com.pucrs.tcc.domain;

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
        ClassificacaoItem item = new ClassificacaoItem("LOGISTICA", "Atraso na entrega");
        ClassificacaoResponse response = new ClassificacaoResponse(List.of(item));

        assertEquals(1, response.getClassificacoes().size());
        assertEquals("LOGISTICA", response.getClassificacoes().get(0).getDepartamento());
        assertEquals("Atraso na entrega", response.getClassificacoes().get(0).getCategoria());
    }

    @Test
    void deveRetornarMultiplasClassificacoes() {
        ClassificacaoItem item1 = new ClassificacaoItem("LOGISTICA", "Produto não entregue");
        ClassificacaoItem item2 = new ClassificacaoItem("ATENDIMENTO_AO_CLIENTE", "Demora no atendimento ou no retorno");

        ClassificacaoResponse response = new ClassificacaoResponse(List.of(item1, item2));

        assertEquals(2, response.getClassificacoes().size());
    }
}
