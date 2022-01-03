package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.domain.EmailStatusEnum.ERROR;
import static br.cams7.tests.ms.domain.EmailStatusEnum.SENT;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.SaveEmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class SendEmailDirectlyUseCaseImpl implements SendEmailDirectlyUseCase {

  private final ModelMapper modelMapper;
  private final SaveEmailRepository saveEmailRepository;
  private final SendEmailService sendEmailService;
  private final CheckIdentificationNumberService checkIdentificationNumberService;

  @Override
  public EmailResponseDTO sendEmail(EmailVO vo) {
    if (!checkIdentificationNumberService.isValid(vo.getIdentificationNumber()))
      throw new InvalidIdentificationNumberException(vo.getIdentificationNumber());

    EmailEntity email = modelMapper.map(vo, EmailEntity.class);
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
    return getEmailResponseDTO(email);
  }

  private EmailEntity save(EmailEntity email) {
    return saveEmailRepository.save(email);
  }

  private EmailResponseDTO getEmailResponseDTO(EmailEntity email) {
    EmailResponseDTO response = modelMapper.map(email, EmailResponseDTO.class);
    response.setIdentificationNumber(email.getOwnerRef());
    return response;
  }
}
