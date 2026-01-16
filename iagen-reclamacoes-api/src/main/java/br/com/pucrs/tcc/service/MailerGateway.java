package br.com.pucrs.tcc.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MailerGateway {
    private final static Logger LOG = Logger.getLogger(MailerGateway.class);

    private final Mailer mailer;

    public MailerGateway(Mailer mailer) {
        this.mailer = mailer;
    }

    public void send(Mail mail) {
        mailer.send(mail);
    }
}
