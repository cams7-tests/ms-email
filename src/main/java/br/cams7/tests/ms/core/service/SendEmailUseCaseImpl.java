package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.domain.EmailStatusEnum.ERROR;
import static br.cams7.tests.ms.core.domain.EmailStatusEnum.SENT;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailUseCase;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class SendEmailUseCaseImpl implements SendEmailUseCase {

  private static final ModelMapper MODEL_MAPPER = new ModelMapper();

  private final EmailRepository emailRepository;
  private final SendEmailService sendEmailService;

  @Override
  public EmailEntity sendEmail(EmailVO vo) {
    EmailEntity email = MODEL_MAPPER.map(vo, EmailEntity.class);
    email.setEmailSentDate(LocalDateTime.now());
    try {
      sendEmailService.sendEmail(email);
      email.setEmailStatus(SENT);
    } catch (SendEmailException e) {
      email.setEmailStatus(ERROR);
    } finally {
      email = save(email);
    }
    return email;
  }

  private EmailEntity save(EmailEntity email) {
    return emailRepository.save(email);
  }
}
