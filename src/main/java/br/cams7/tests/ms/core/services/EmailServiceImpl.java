package br.cams7.tests.ms.core.services;

import static br.cams7.tests.ms.core.domain.StatusEmail.ERROR;
import static br.cams7.tests.ms.core.domain.StatusEmail.SENT;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.domain.PageInfo;
import br.cams7.tests.ms.core.ports.EmailRepositoryPort;
import br.cams7.tests.ms.core.ports.EmailServicePort;
import br.cams7.tests.ms.core.ports.SendEmailServicePort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailServiceImpl implements EmailServicePort {

  private final EmailRepositoryPort emailRepositoryPort;
  private final SendEmailServicePort sendEmailServicePort;

  @SuppressWarnings("finally")
  @Override
  public EmailEntity sendEmail(EmailEntity email) {
    email.setSendDateEmail(LocalDateTime.now());
    try {
      sendEmailServicePort.sendEmailSmtp(email);
      email.setStatusEmail(SENT);
    } catch (Exception e) {
      email.setStatusEmail(ERROR);
    } finally {
      return save(email);
    }
  }

  @Override
  public List<EmailEntity> findAll(PageInfo pageInfo) {
    // inserir manipulação de dados/regras
    return emailRepositoryPort.findAll(pageInfo);
  }

  @Override
  public Optional<EmailEntity> findById(UUID emailId) {
    // inserir manipulação de dados/regras
    return emailRepositoryPort.findById(emailId);
  }

  @Override
  public EmailEntity save(EmailEntity email) {
    return emailRepositoryPort.save(email);
  }
}
