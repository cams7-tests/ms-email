package br.cams7.tests.ms.infra.smtp;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;

import org.springframework.mail.SimpleMailMessage;

public class SimpleMailMessageTestData {
  public static SimpleMailMessage defaultSimpleMailMessage() {
    var message = new SimpleMailMessage();
    message.setFrom(EMAIL_FROM);
    message.setTo(EMAIL_TO);
    message.setSubject(MESSAGE_SUBJECT);
    message.setText(MESSAGE_TEXT);
    return message;
  }
}
