package br.com.pucrs.tcc.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class
MailServiceTest {

    @Inject
    MailService mailService;

    @Inject
    MockMailbox mailbox;

    @BeforeEach
    void setUp() {
        mailbox.clear();
    }

    @Test
    void deveEnviarEmailComProtocolo() {
        // Arrange
        String destinatario = "usuario@teste.com";
        String protocolo = "ABC-123-XYZ";
        String nome = "João Silva";

        // Act
        mailService.enviarNotificacaoProtocolo(destinatario, protocolo, nome);

        // Assert
        List<Mail> sent = mailbox.getMailsSentTo(destinatario);
        assertEquals(1, sent.size(), "Deve ter enviado exatamente 1 email");

        Mail email = sent.get(0);
        assertTrue(email.getSubject().contains(protocolo), "Assunto deve conter o protocolo");
        assertTrue(email.getText().contains(protocolo), "Corpo deve conter o protocolo");
        assertTrue(email.getText().contains(nome), "Corpo deve conter o nome do usuário");
    }

    @Test
    void deveConterInformacoesObrigatoriasNoEmail() {
        // Arrange
        String destinatario = "teste@email.com";
        String protocolo = "PROTO-001";
        String nome = "Maria";

        // Act
        mailService.enviarNotificacaoProtocolo(destinatario, protocolo, nome);

        // Assert
        List<Mail> sent = mailbox.getMailsSentTo(destinatario);
        Mail email = sent.get(0);

        String corpo = email.getText();
        assertTrue(corpo.contains("Olá, " + nome), "Deve iniciar com saudação personalizada");
        assertTrue(corpo.contains("registrada com sucesso"), "Deve confirmar o registro");
        assertTrue(corpo.contains("Protocolo: " + protocolo), "Deve exibir protocolo formatado");
        assertTrue(corpo.contains("acompanhar o status"), "Deve informar sobre acompanhamento");
    }

    @Test
    void naoDeveLancarExcecaoSeEmailFalhar() {
        // Este teste valida que exceções são tratadas internamente

        assertDoesNotThrow(() -> {
            mailService.enviarNotificacaoProtocolo("invalido", "PROTO", "Teste");
        });
    }
}