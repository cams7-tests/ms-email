package br.cams7.tests.ms.infra.smtp;

import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMTPSendEmailService implements SendEmailService {

  private final JavaMailSender emailSender;

  @Override
  public void sendEmail(EmailEntity email) throws SendEmailException {
    var message = new SimpleMailMessage();
    message.setFrom(email.getEmailFrom());
    message.setTo(email.getEmailTo());
    message.setSubject(email.getSubject());
    message.setText(email.getText());
    try {
      emailSender.send(message);
    } catch (MailException e) {
      throw new SendEmailException(e.getMessage(), e.getCause());
    }
  }
}
