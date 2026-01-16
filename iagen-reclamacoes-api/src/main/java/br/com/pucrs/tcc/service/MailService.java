package br.com.pucrs.tcc.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MailService {

    private static final Logger LOG = Logger.getLogger(MailService.class);

    @Inject
    MailerGateway mailerGateway;

    /**
     * Envia email de notificação com protocolo da reclamação
     *
     * @param destinatario Email do usuário
     * @param protocolo Protocolo gerado
     * @param nomeUsuario Nome do usuário
     */
    public void enviarNotificacaoProtocolo(String destinatario, String protocolo, String nomeUsuario) {
        try {
            String assunto = "Reclamação Registrada - Protocolo: " + protocolo;
            String corpo = construirCorpoEmail(nomeUsuario, protocolo);

            mailerGateway.send(
                    Mail.withText(destinatario, assunto, corpo)
            );

            LOG.infof("Email enviado com sucesso para %s - Protocolo: %s", destinatario, protocolo);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao enviar email para %s - Protocolo: %s", destinatario, protocolo);
            // Não propaga exceção - falha de email não deve interromper o fluxo
        }
    }

    private String construirCorpoEmail(String nomeUsuario, String protocolo) {
        return String.format("""
            Olá, %s!
            
            Sua reclamação foi registrada com sucesso.
            
            Protocolo: %s
            
            Você pode acompanhar o status da sua reclamação usando este protocolo.
            
            Atenciosamente,
            Equipe IAGen
            """, nomeUsuario, protocolo);
    }
}