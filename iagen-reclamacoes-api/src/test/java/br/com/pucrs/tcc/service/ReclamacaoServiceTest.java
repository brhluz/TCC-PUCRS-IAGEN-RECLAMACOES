package br.com.pucrs.tcc.service;

import br.com.pucrs.tcc.domain.ai.ClassificacaoItem;
import br.com.pucrs.tcc.domain.ai.ClassificacaoResponse;
import br.com.pucrs.tcc.domain.ai.ReclamacaoAiService;
import br.com.pucrs.tcc.domain.ai.Taxonomia;
import br.com.pucrs.tcc.domain.dto.ReclamacaoClassificadaDTO;
import br.com.pucrs.tcc.domain.entity.Reclamacao;
import br.com.pucrs.tcc.resource.dto.ReclamacaoRequest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ReclamacaoServiceTest {

    @Inject
    ReclamacaoService reclamacaoService;

    @InjectMock
    ReclamacaoAiService aiService;

    @BeforeEach
    void setupAiMock() {
        ClassificacaoItem item = new ClassificacaoItem();
        item.setDepartamento(Taxonomia.Departamento.ATENDIMENTO_CLIENTE);
        item.setCategoria(Taxonomia.Categoria.PRODUTO_COM_DEFEITO);
        item.setMotivoExtraido("Produto com defeito");

        ClassificacaoResponse response = new ClassificacaoResponse();
        response.setClassificacoes(List.of(item));

        when(aiService.classificar(anyString())).thenReturn(response);
    }

    @Test
    @Transactional
    public void deveCriarReclamacaoComDadosValidos() {
        // Arrange
        ReclamacaoRequest request = new ReclamacaoRequest();
        request.setNome("João Silva");
        request.setEmail("joao@teste.com");
        request.setDescricao("Produto chegou com defeito");

        // Act
        Reclamacao reclamacao = reclamacaoService.criar(request);

        // Assert
        assertNotNull(reclamacao);
        assertNotNull(reclamacao.getProtocolo());
        assertEquals("João Silva", reclamacao.getNome());
        assertEquals("joao@teste.com", reclamacao.getEmail());
        assertEquals("Produto chegou com defeito", reclamacao.getDescricao());
        assertEquals(Reclamacao.StatusReclamacao.RECEBIDA, reclamacao.getStatus());
        assertNotNull(reclamacao.getCriadoEm());
    }

    @Test
    @Transactional
    public void deveGerarProtocoloUnicoParaCadaReclamacao() {
        // Arrange
        ReclamacaoRequest request1 = new ReclamacaoRequest();
        request1.setNome("João Silva");
        request1.setEmail("joao@teste.com");
        request1.setDescricao("Problema 1");

        ReclamacaoRequest request2 = new ReclamacaoRequest();
        request2.setNome("Maria Santos");
        request2.setEmail("maria@teste.com");
        request2.setDescricao("Problema 2");

        // Act
        Reclamacao reclamacao1 = reclamacaoService.criar(request1);
        Reclamacao reclamacao2 = reclamacaoService.criar(request2);

        // Assert
        assertNotNull(reclamacao1.getProtocolo());
        assertNotNull(reclamacao2.getProtocolo());
        assertNotEquals(reclamacao1.getProtocolo(), reclamacao2.getProtocolo());
    }

    @Test
    @Transactional
    public void devePersistirReclamacaoNoBancoDeDados() {
        // Arrange
        ReclamacaoRequest request = new ReclamacaoRequest();
        request.setNome("Pedro Costa");
        request.setEmail("pedro@teste.com");
        request.setDescricao("Entrega atrasada");

        // Act
        Reclamacao reclamacao = reclamacaoService.criar(request);

        // Assert
        long count = Reclamacao.count("descricao = ?1", "Entrega atrasada");
        assertTrue(count > 0, "Reclamação deve estar persistida");
    }

    @Test
    @Transactional
    public void deveRetornarReclamacaoClassificadaQuandoProtocoloExiste() {
        // Arrange
        ReclamacaoRequest request = new ReclamacaoRequest();
        request.setNome("Ana Silva");
        request.setEmail("ana@teste.com");
        request.setDescricao("Produto com defeito");

        Reclamacao reclamacaoCriada = reclamacaoService.criar(request);
        String protocolo = reclamacaoCriada.getProtocolo();

        // Act
        ReclamacaoClassificadaDTO resultado = reclamacaoService.retornarReclamacaoClassificadaPorProtocolo(protocolo);

        // Assert
        assertNotNull(resultado);
        assertEquals(protocolo, resultado.getProtocolo());
        assertEquals("Produto com defeito", resultado.getDescricao());
        assertTrue(resultado.isClassificada());
        assertEquals("RECEBIDA", resultado.getStatusAtendimento());
        assertNotNull(resultado.getUltimaAtualizacao());
        assertNotNull(resultado.getCategoriasPorDepartamento());
    }

    @Test
    @Transactional
    public void deveIndicarNaoClassificadaQuandoStatusPendenteTriagemHumana() {
        // Arrange
        ReclamacaoRequest request = new ReclamacaoRequest();
        request.setNome("Carlos Santos");
        request.setEmail("carlos@teste.com");
        request.setDescricao("Descrição ambígua");

        // Simular falha na classificação
        when(aiService.classificar(anyString())).thenReturn(new ClassificacaoResponse());

        Reclamacao reclamacaoCriada = reclamacaoService.criar(request);
        String protocolo = reclamacaoCriada.getProtocolo();

        // Act
        ReclamacaoClassificadaDTO resultado = reclamacaoService.retornarReclamacaoClassificadaPorProtocolo(protocolo);

        // Assert
        assertNotNull(resultado);
        assertEquals(protocolo, resultado.getProtocolo());
        assertFalse(resultado.isClassificada());
        assertEquals("PENDENTE_TRIAGEM_HUMANA", resultado.getStatusAtendimento());
    }

    @Test
    @Transactional
    public void deveLancarExcecaoQuandoProtocoloNaoExiste() {
        // Arrange
        String protocoloInexistente = "00000000-0000-0000-0000-000000000000";

        // Act & Assert
        WebApplicationException exception = assertThrows(
            WebApplicationException.class,
            () -> reclamacaoService.retornarReclamacaoClassificadaPorProtocolo(protocoloInexistente)
        );

        assertEquals(404, exception.getResponse().getStatus());
        assertEquals("Reclamação não encontrada", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveRetornarClassificacoesCorretamente() {
        // Arrange
        ClassificacaoItem item1 = new ClassificacaoItem();
        item1.setDepartamento(Taxonomia.Departamento.LOGISTICA);
        item1.setCategoria(Taxonomia.Categoria.ATRASO_ENTREGA);
        item1.setMotivoExtraido("Atraso na entrega");

        ClassificacaoItem item2 = new ClassificacaoItem();
        item2.setDepartamento(Taxonomia.Departamento.PRODUTO_TECNICO);
        item2.setCategoria(Taxonomia.Categoria.PRODUTO_COM_DEFEITO);
        item2.setMotivoExtraido("Produto com defeito");

        ClassificacaoResponse response = new ClassificacaoResponse();
        response.setClassificacoes(List.of(item1, item2));
        when(aiService.classificar(anyString())).thenReturn(response);

        ReclamacaoRequest request = new ReclamacaoRequest();
        request.setNome("Maria Oliveira");
        request.setEmail("maria@teste.com");
        request.setDescricao("Produto chegou atrasado e com defeito");

        Reclamacao reclamacaoCriada = reclamacaoService.criar(request);
        String protocolo = reclamacaoCriada.getProtocolo();

        // Act
        ReclamacaoClassificadaDTO resultado = reclamacaoService.retornarReclamacaoClassificadaPorProtocolo(protocolo);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isClassificada());
        assertNotNull(resultado.getCategoriasPorDepartamento());
        assertEquals(2, resultado.getCategoriasPorDepartamento().size());
    }
}
