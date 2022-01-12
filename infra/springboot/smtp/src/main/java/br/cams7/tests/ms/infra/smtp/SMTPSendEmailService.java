package br.cams7.tests.ms.infra.smtp;

import static br.cams7.tests.ms.domain.EmailStatusEnum.ERROR;
import static br.cams7.tests.ms.domain.EmailStatusEnum.SENT;
import static java.util.logging.Level.WARNING;

import br.cams7.tests.ms.core.port.out.SendEmailGateway;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log
@Service
@RequiredArgsConstructor
public class SMTPSendEmailService implements SendEmailGateway {

  private final JavaMailSender emailSender;

  @Override
  public Mono<EmailStatusEnum> sendEmail(EmailEntity email) {
    return Mono.fromCallable(
        () -> {
          var message = new SimpleMailMessage();
          message.setFrom(email.getEmailFrom());
          message.setTo(email.getEmailTo());
          message.setSubject(email.getSubject());
          message.setText(email.getText());
          try {
            emailSender.send(message);
            return SENT;
          } catch (MailException e) {
            log.log(WARNING, "An error occurred while sending the email", e.getCause());
            return ERROR;
          }
        });
  }
}
