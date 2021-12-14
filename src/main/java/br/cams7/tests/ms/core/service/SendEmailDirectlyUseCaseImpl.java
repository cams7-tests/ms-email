package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.domain.EmailStatusEnum.ERROR;
import static br.cams7.tests.ms.core.domain.EmailStatusEnum.SENT;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class SendEmailDirectlyUseCaseImpl implements SendEmailDirectlyUseCase {

  private static final ModelMapper MODEL_MAPPER = new ModelMapper();

  private final EmailRepository emailRepository;
  private final SendEmailService sendEmailService;
  private final CheckIdentificationNumberService checkIdentificationNumberService;

  @Override
  public EmailEntity sendEmail(EmailVO vo) {
    if (!checkIdentificationNumberService.isValid(vo.getIdentificationNumber()))
      throw new InvalidIdentificationNumberException(vo.getIdentificationNumber());

    EmailEntity email = MODEL_MAPPER.map(vo, EmailEntity.class);
    email.setOwnerRef(vo.getIdentificationNumber());

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
