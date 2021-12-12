package br.cams7.tests.ms.core.services;

import br.cams7.tests.ms.core.domain.Email;
import br.cams7.tests.ms.core.domain.PageInfo;
import br.cams7.tests.ms.core.domain.StatusEmail;
import br.cams7.tests.ms.core.ports.EmailRepositoryPort;
import br.cams7.tests.ms.core.ports.EmailServicePort;
import br.cams7.tests.ms.core.ports.SendEmailServicePort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EmailServiceImpl implements EmailServicePort {

  private final EmailRepositoryPort emailRepositoryPort;
  private final SendEmailServicePort sendEmailServicePort;

  public EmailServiceImpl(
      final EmailRepositoryPort emailRepositoryPort,
      final SendEmailServicePort sendEmailServicePort) {
    this.emailRepositoryPort = emailRepositoryPort;
    this.sendEmailServicePort = sendEmailServicePort;
  }

  @SuppressWarnings("finally")
  @Override
  public Email sendEmail(Email email) {
    email.setSendDateEmail(LocalDateTime.now());
    try {
      sendEmailServicePort.sendEmailSmtp(email);
      email.setStatusEmail(StatusEmail.SENT);
    } catch (Exception e) {
      email.setStatusEmail(StatusEmail.ERROR);
    } finally {
      return save(email);
    }
  }

  @Override
  public List<Email> findAll(PageInfo pageInfo) {
    // inserir manipulação de dados/regras
    return emailRepositoryPort.findAll(pageInfo);
  }

  @Override
  public Optional<Email> findById(UUID emailId) {
    // inserir manipulação de dados/regras
    return emailRepositoryPort.findById(emailId);
  }

  @Override
  public Email save(Email email) {
    return emailRepositoryPort.save(email);
  }
}
