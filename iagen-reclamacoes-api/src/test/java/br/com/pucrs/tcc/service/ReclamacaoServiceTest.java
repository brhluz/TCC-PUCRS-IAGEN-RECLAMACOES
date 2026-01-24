package br.com.pucrs.tcc.service;

import br.com.pucrs.tcc.domain.entity.Reclamacao;
import br.com.pucrs.tcc.resource.dto.ReclamacaoRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ReclamacaoServiceTest {

    @Inject
    ReclamacaoService reclamacaoService;

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
}
