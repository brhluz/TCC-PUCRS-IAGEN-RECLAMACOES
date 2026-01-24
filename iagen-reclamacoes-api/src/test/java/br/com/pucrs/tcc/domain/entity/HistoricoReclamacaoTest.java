package br.com.pucrs.tcc.domain.entity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class HistoricoReclamacaoTest {

    @Test
    @Transactional
    public void deveRegistrarMudancaDeStatus() {
        // Arrange
        Reclamacao reclamacao = new Reclamacao();
        reclamacao.setNome("João Silva");
        reclamacao.setEmail("joao@teste.com");
        reclamacao.setDescricao("Teste");
        reclamacao.persist();

        // Act
        HistoricoReclamacao historico = new HistoricoReclamacao();
        historico.setReclamacao(reclamacao);
        historico.setStatusAnterior(null);
        historico.setStatusNovo(Reclamacao.StatusReclamacao.RECEBIDA);
        historico.setObservacao("Reclamação criada");
        historico.persist();

        // Assert
        assertNotNull(historico.getDataAlteracao());
        assertEquals(Reclamacao.StatusReclamacao.RECEBIDA, historico.getStatusNovo());
        assertEquals(reclamacao, historico.getReclamacao());
    }

    @Test
    @Transactional
    public void devePermitirStatusAnteriorNulo() {
        // Arrange
        Reclamacao reclamacao = new Reclamacao();
        reclamacao.setNome("Maria");
        reclamacao.setEmail("maria@teste.com");
        reclamacao.setDescricao("Teste");
        reclamacao.persist();

        // Act
        HistoricoReclamacao historico = new HistoricoReclamacao();
        historico.setReclamacao(reclamacao);
        historico.setStatusAnterior(null);
        historico.setStatusNovo(Reclamacao.StatusReclamacao.RECEBIDA);
        historico.persist();

        // Assert
        assertNull(historico.getStatusAnterior());
        assertNotNull(historico.getStatusNovo());
    }

    @Test
    @Transactional
    public void deveRegistrarTransicaoDeStatus() {
        // Arrange
        Reclamacao reclamacao = new Reclamacao();
        reclamacao.setNome("Pedro");
        reclamacao.setEmail("pedro@teste.com");
        reclamacao.setDescricao("Teste");
        reclamacao.persist();

        // Act
        HistoricoReclamacao historico = new HistoricoReclamacao();
        historico.setReclamacao(reclamacao);
        historico.setStatusAnterior(Reclamacao.StatusReclamacao.RECEBIDA);
        historico.setStatusNovo(Reclamacao.StatusReclamacao.EM_ATENDIMENTO);
        historico.setObservacao("Atendimento iniciado");
        historico.persist();

        // Assert
        assertEquals(Reclamacao.StatusReclamacao.RECEBIDA, historico.getStatusAnterior());
        assertEquals(Reclamacao.StatusReclamacao.EM_ATENDIMENTO, historico.getStatusNovo());
        assertEquals("Atendimento iniciado", historico.getObservacao());
    }
}
