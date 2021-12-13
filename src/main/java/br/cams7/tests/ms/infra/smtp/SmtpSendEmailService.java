package br.cams7.tests.ms.infra.smtp;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.ports.out.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmtpSendEmailService implements SendEmailService {

  private final JavaMailSender emailSender;

  @Autowired
  SmtpSendEmailService(JavaMailSender emailSender) {
    super();
    this.emailSender = emailSender;
  }

  @Override
  public void sendEmailSmtp(EmailEntity email) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(email.getEmailFrom());
    message.setTo(email.getEmailTo());
    message.setSubject(email.getSubject());
    message.setText(email.getText());
    emailSender.send(message);
  }
}
