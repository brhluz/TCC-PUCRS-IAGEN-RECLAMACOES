package br.com.pucrs.tcc.service;

import br.com.pucrs.tcc.domain.ai.ClassificacaoItem;
import br.com.pucrs.tcc.domain.ai.ClassificacaoResponse;
import br.com.pucrs.tcc.domain.ai.Taxonomia;
import br.com.pucrs.tcc.domain.ai.ReclamacaoAiService;
import br.com.pucrs.tcc.domain.exception.ClassificacaoException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
class ClassificacaoServiceTest {

    @Inject
    ClassificacaoService service;

    @InjectMock
    ReclamacaoAiService aiService;

    @Test
    @DisplayName("Deve classificar reclamação simples de logística")
    void deveClassificarReclamacaoLogistica() {
        // Given
        String descricao = "Meu pedido não chegou no prazo prometido";

        ClassificacaoItem item = new ClassificacaoItem(
            Taxonomia.Departamento.LOGISTICA,
            Taxonomia.Categoria.ATRASO_ENTREGA,
            "Pedido não entregue no prazo"
        );

        ClassificacaoResponse aiResponse = new ClassificacaoResponse();
        aiResponse.setClassificacoes(List.of(item));

        when(aiService.classificar(anyString())).thenReturn(aiResponse);

        // When
        ClassificacaoResponse resultado = service.classificar(descricao);

        // Then
        Mockito.verify(aiService).classificar(descricao);

        assertNotNull(resultado);
        assertNotNull(resultado.getClassificacoes());
        assertEquals(1, resultado.getClassificacoes().size());
        assertEquals(Taxonomia.Departamento.LOGISTICA, resultado.getClassificacoes().get(0).getDepartamento());
        assertEquals(Taxonomia.Categoria.ATRASO_ENTREGA, resultado.getClassificacoes().get(0).getCategoria());
    }

    @Test
    @DisplayName("Deve classificar reclamação com múltiplos departamentos (split)")
    void deveClassificarComMultiplosDepartamentos() {
        // Given
        String descricao = "O produto chegou atrasado e veio com defeito";

        ClassificacaoItem itemLogistica = new ClassificacaoItem(
            Taxonomia.Departamento.LOGISTICA,
            Taxonomia.Categoria.ATRASO_ENTREGA,
            "Produto chegou atrasado"
        );

        ClassificacaoItem itemTecnico = new ClassificacaoItem(
            Taxonomia.Departamento.PRODUTO_TECNICO,
            Taxonomia.Categoria.PRODUTO_COM_DEFEITO,
            "Produto veio com defeito"
        );

        ClassificacaoResponse aiResponse = new ClassificacaoResponse();
        aiResponse.setClassificacoes(List.of(itemLogistica, itemTecnico));

        when(aiService.classificar(anyString())).thenReturn(aiResponse);

        // When
        ClassificacaoResponse resultado = service.classificar(descricao);

        // Then
        Mockito.verify(aiService).classificar(descricao);
        assertNotNull(resultado);
        assertEquals(2, resultado.getClassificacoes().size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando IA não tem segurança")
    void deveRetornarVazioQuandoIaSemSeguranca() {
        String descricao = "Texto ambíguo sem contexto claro";

        ClassificacaoResponse aiResponse = new ClassificacaoResponse();
        aiResponse.setClassificacoes(Collections.emptyList());

        when(aiService.classificar(descricao)).thenReturn(aiResponse);

        ClassificacaoResponse resultado = service.classificar(descricao);

        assertNotNull(resultado);
        assertNotNull(resultado.getClassificacoes());
        assertTrue(resultado.getClassificacoes().isEmpty());

        Mockito.verify(aiService).classificar(descricao);
        Mockito.verifyNoMoreInteractions(aiService);
    }

    @Test
    @DisplayName("Deve lançar ClassificacaoException quando ocorrer erro interno/timeout não-HTTP")
    void deveTratarTimeoutDaIA() {
        String descricao = "Produto não chegou";

        when(aiService.classificar(descricao)).thenThrow(new RuntimeException("Timeout"));

        assertThrows(ClassificacaoException.class, () -> service.classificar(descricao));

        Mockito.verify(aiService).classificar(descricao);
        Mockito.verifyNoMoreInteractions(aiService);
    }

    @Test
    @DisplayName("Deve propagar WebApplicationException quando provedor retornar 429")
    void devePropagarWebApplicationException429() {
        String descricao = "Produto não chegou";

        WebApplicationException ex = new WebApplicationException(
                Response.status(429).build()
        );

        when(aiService.classificar(descricao)).thenThrow(ex);

        WebApplicationException thrown =
                assertThrows(WebApplicationException.class, () -> service.classificar(descricao));

        assertEquals(429, thrown.getResponse().getStatus());

        Mockito.verify(aiService).classificar(descricao);
        Mockito.verifyNoMoreInteractions(aiService);
    }
}
