package br.com.pucrs.tcc.service;

import io.quarkus.mailer.Mail;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class MailServiceTest {

    @Inject
    MailService mailService;

    @InjectMock
    MailerGateway mailerGateway;

    @Test
    void deveEnviarEmailComProtocolo() {
        mailService.enviarNotificacaoProtocolo("usuario@teste.com", "PROTO-001", "Maria");

        verify(mailerGateway, times(1)).send(any(Mail.class));
    }

    @Test
    void deveConterInformacoesObrigatoriasNoEmail() {
        ArgumentCaptor<Mail> captor = ArgumentCaptor.forClass(Mail.class);

        mailService.enviarNotificacaoProtocolo("usuario@teste.com", "PROTO-001", "Maria");

        verify(mailerGateway).send(captor.capture());
        Mail mail = captor.getValue();

        assertTrue(mail.getSubject() != null && mail.getSubject().contains("PROTO-001"));
        assertTrue(mail.getText() != null && mail.getText().contains("Maria"));
        assertTrue(mail.getText().contains("PROTO-001"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoMailerFalhar() {
        doThrow(new RuntimeException("Falha simulada no SMTP"))
                .when(mailerGateway)
                .send(any(Mail.class));

        assertDoesNotThrow(() ->
                mailService.enviarNotificacaoProtocolo("usuario@teste.com", "PROTO-001", "Maria")
        );
    }
}
