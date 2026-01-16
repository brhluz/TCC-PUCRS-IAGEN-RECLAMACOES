package br.com.pucrs.tcc.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@QuarkusTest
public class MailServiceIntegrationTest {
    @Inject
    MailService mailService;

    @Test
    void deveExecutarFluxoSemDependerDeSmtpReal() {

        assertDoesNotThrow(() ->
                mailService.enviarNotificacaoProtocolo("usuario@teste.com", "PROTO-INT-001", "Maria")
        );
    }
}
