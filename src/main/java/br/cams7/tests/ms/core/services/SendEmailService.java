package br.cams7.tests.ms.core.services;

import static br.cams7.tests.ms.core.domain.StatusEmail.ERROR;
import static br.cams7.tests.ms.core.domain.StatusEmail.SENT;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.ports.in.SendEmailUseCase;
import br.cams7.tests.ms.core.ports.out.EmailRepositoryPort;
import br.cams7.tests.ms.core.ports.out.SendEmailServicePort;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SendEmailService implements SendEmailUseCase {

  private final EmailRepositoryPort emailRepositoryPort;
  private final SendEmailServicePort sendEmailServicePort;

  @Override
  public EmailEntity sendEmail(EmailEntity email) {
    email.setSendDateEmail(LocalDateTime.now());
    try {
      sendEmailServicePort.sendEmailSmtp(email);
      email.setStatusEmail(SENT);
    } catch (Exception e) {
      email.setStatusEmail(ERROR);
    } finally {
      email = save(email);
    }
    return email;
  }

  private EmailEntity save(EmailEntity email) {
    return emailRepositoryPort.save(email);
  }
}
